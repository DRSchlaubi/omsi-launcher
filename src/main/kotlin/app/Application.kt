package dev.nycode.omsilauncher.app

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.LocalStrings
import cafe.adriel.lyricist.ProvideStrings
import cafe.adriel.lyricist.rememberStrings
import compose.icons.TablerIcons
import compose.icons.tablericons.Stack
import dev.nycode.omsilauncher.config.config
import dev.nycode.omsilauncher.instance.Instance
import dev.nycode.omsilauncher.omsi.OmsiProcessState
import dev.nycode.omsilauncher.omsi.receiveOmsiProcessUpdates
import dev.nycode.omsilauncher.ui.components.InstanceListEntry
import dev.nycode.omsilauncher.ui.components.PathInputField
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.nio.file.Path
import java.util.*
import kotlin.io.path.createDirectories
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.notExists

@Composable
fun Application() {
    val lyricist = rememberStrings()
    val scope = rememberCoroutineScope()
    val applicationState = rememberApplicationState()
    val instances = applicationState.instances
    val omsiState by receiveOmsiProcessUpdates().collectAsState(
        OmsiProcessState.NOT_RUNNING,
        Dispatchers.IO
    )
    ProvideStrings(lyricist) {
        val strings = LocalStrings.current
        Row(Modifier.fillMaxSize()) {
            NavigationRail {
                NavigationRailItem(
                    true,
                    onClick = {},
                    icon = { Icon(TablerIcons.Stack, strings.instances) },
                    label = { Text(strings.instances) }
                )
            }
            Box(modifier = Modifier.fillMaxSize()) {
                val stateVertical = rememberScrollState(0)
                Box(
                    Modifier.fillMaxSize()
                        .verticalScroll(stateVertical)
                        .padding(end = 12.dp, bottom = 12.dp)
                ) {
                    Column(Modifier.fillMaxSize()) {
                        instances.forEach {
                            InstanceListEntry(
                                Modifier.padding(5.dp),
                                applicationState,
                                it,
                                scope,
                                omsiState
                            )
                        }
                        InstanceCreationCard(
                            Modifier.padding(5.dp),
                            scope,
                            applicationState,
                            { name, patchVersion, directory ->
                                directory.parent.createDirectories()
                                applicationState.createNewInstance(
                                    UUID.randomUUID(),
                                    name,
                                    directory,
                                    patchVersion,
                                    uses4GBPatch = true
                                )
                            },
                            omsiState
                        )
                    }
                }
                VerticalScrollbar(
                    modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                    adapter = rememberScrollbarAdapter(stateVertical)
                )
            }
        }
    }
}

class InstanceCreationState {
    var name: MutableState<String> = mutableStateOf(DEFAULT_NAME)

    var customDirectory: MutableState<Path?> = mutableStateOf(DEFAULT_CUSTOM_DIRECTORY)
    var patchVersion: MutableState<Instance.PatchVersion> =
        mutableStateOf(DEFAULT_PATCH_VERSION)

    fun clear() {
        name.value = DEFAULT_NAME
        customDirectory.value = DEFAULT_CUSTOM_DIRECTORY
        patchVersion.value = DEFAULT_PATCH_VERSION
    }

    companion object {
        private const val DEFAULT_NAME = "New Instance"
        private val DEFAULT_CUSTOM_DIRECTORY: Path? = null
        private val DEFAULT_PATCH_VERSION: Instance.PatchVersion =
            Instance.PatchVersion.BI_ARTICULATED_BUS_VERSION
    }
}

@Composable
fun InstanceCreationCard(
    modifier: Modifier,
    scope: CoroutineScope,
    applicationState: ApplicationState,
    createInstance: suspend (name: String, patchVersion: Instance.PatchVersion, directory: Path) -> Unit,
    omsiState: OmsiProcessState,
) = Card(modifier = modifier.fillMaxWidth().height(400.dp), elevation = 3.dp) {
    val strings = LocalStrings.current
    val state = remember { InstanceCreationState() }
    val (name, setName) = state.name
    val patchVersion by state.patchVersion
    val (customDirectory, setCustomDirectory) = state.customDirectory
    val directory = customDirectory ?: config.rootInstallation.resolve("instances")
        .resolve(name.sanitizePath())
    val isValidDirectory = directory.notExists() || directory.listDirectoryEntries()
        .isEmpty()
    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            OutlinedTextField(
                modifier = Modifier.padding(10.dp),
                value = name,
                onValueChange = setName,
                label = {
                    Text(strings.instanceName)
                },
                singleLine = true,
            )
            PathInputField(
                value = directory,
                onValueChange = setCustomDirectory,
                defaultDirectory = config.rootInstallation.resolve("instances"),
                modifier = Modifier.padding(10.dp),
                isError = !isValidDirectory,
                label = {
                    if (isValidDirectory) {
                        Text("Instance Directory")
                    } else {
                        Text("Directory must be empty.")
                    }
                }
            )
        }
        val createInstanceFromForm: () -> Unit = {
            state.clear()
            scope.launch(Dispatchers.IO) {
                createInstance(name, patchVersion, directory)
            }
        }
        Button(
            createInstanceFromForm,
            enabled = omsiState != OmsiProcessState.RUNNING && name.isNotBlank() && isValidDirectory,
            modifier = Modifier.align(Alignment.BottomEnd)
                .padding(10.dp),
        ) {
            Text(strings.addNewInstance)
        }
    }
}

private val sanitizingRegex = Regex("[^\\w_-]+")

private fun String.sanitizePath(): String {
    return replace(sanitizingRegex, "_")
}
