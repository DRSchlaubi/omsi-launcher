package dev.nycode.omsilauncher.localization

import androidx.compose.ui.text.AnnotatedString

data class Strings(
    val ok: String,
    val `continue`: String,
    val instances: String,
    val settings: String,
    val addNewInstance: String,
    val eCitaro: String = "eCitaro",
    val runInstance: String,
    val launch: String,
    val runEditor: String,
    val launchEditor: String,
    val instancePatchVersion: String,
    val edit: String,
    val delete: String,
    val deleteConfirmation: String,
    val yes: String,
    val no: String,
    val confirmDeletion: (instance: String) -> String,
    val instanceName: String,
    val activateInstance: String,
    val activate: String,
    val closeSteamInfo: String,
    val closeSteam: String,
    val closeSteamLaunchInfo: String,
    val unableToCloseSteam: String,
    val unableToCloseSteamTitle: String,
    val createBaseGame: String,
    val baseGameExplanation: String,
    val setup: String,
    val setupChooseDirectory: String,
    val setupChooseDirectoryWarning: String,
    val chooseGameFileDirectory: String,
    val noGameFileDirectory: String,
    val newInstance: String,
    val biArticulatedBusPatch: String,
    val tramPatch: String,
    val createANewInstance: String,
    val instanceDirectory: String,
    val patchVersion: String,
    val use4gbPatch: String,
    val use4gbPatchTooltip: String,
    val createInstance: String,
    val continueSetup: String,
    val setupLauncherDirectoryHeadline: String,
    val setupOmsiDirectoryHeadline: String,
    val setupStepCopyGameFiles: String,
    val setupStepCopyManifest: String,
    val setupStepSavingConfiguration: String,
    val setupStepCreatingInstancesJson: String,
    val startInstance: String,
    val startEditorInstance: String,
    val selectedPatchVersion: (patchVersion: String) -> String,
    val editInstance: String,
    val deleteInstance: String,
    val omsiNotInstalledTitle: String,
    val omsiNotInstalledDescription: String,
    val steamNotInstalledTitle: String,
    val steamNotInstalledDescription: String,
    val saveLogs: String,
    val saveLogsTooltip: (instanceLocation: String?) -> String,
    val debugMode: String,
    val debugModeTooltip: String,
    val defaultLogLevel: String,
    val noLogLogLevel: String,
    val logAllLogLevel: String,
    val defaultWindowMode: String,
    val windowedWindowMode: String,
    val fullScreenWindowMode: String,
    val logLevel: String,
    val screenMode: String,
    val showInstanceFiles: String,
    val cannotDeleteMainInstance: String,
    val baseInstance: String,
    val directoryNeedsToBeEmpty: String,
    val reSyncInstance: String,
    val reSyncInstanceExplainer: AnnotatedString,
    val preparingReSynchronisation: String,
    val waitingForChanges: String,
    val iAmDone: String,
    val mergingSteamManifest: (name: String) -> String,
    val reLinkingInstances: String,
    val save: String,
    val editInstanceTitle: (name: String) -> String,
    val editBaseInstance: String,
    val instanceIcon: String,
    val clickToChangeInstanceIcon: String,
    val onlyImages: String,
    val reset: String
)
