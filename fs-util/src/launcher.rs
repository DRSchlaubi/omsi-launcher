use deelevate::{BridgeServer, PrivilegeLevel, Token};

pub fn with_privileges<T>(run: T) -> std::io::Result<()> where T: Fn() -> std::io::Result<()> {
    let token = Token::with_current_process()?;
    match token.privilege_level()? {
        PrivilegeLevel::NotPrivileged => {
            spawn_with_elevated_privileges()
        }
        PrivilegeLevel::Elevated => run(),
        PrivilegeLevel::HighIntegrityAdmin => run()
    }
}

fn spawn_with_elevated_privileges() -> std::io::Result<()> {
    let token = Token::with_current_process()?;
    let level = token.privilege_level()?;

    let target_token = match level {
        PrivilegeLevel::NotPrivileged => token.as_medium_integrity_safer_token()?,
        PrivilegeLevel::HighIntegrityAdmin | PrivilegeLevel::Elevated => return Ok(()),
    };

    let mut server = BridgeServer::new();
    let mut argv = std::env::args_os().collect();
    let mut bridge_cmd = server.start_for_command(&mut argv, &target_token)?;
    let proc = bridge_cmd.shell_execute("runas")?;
    std::process::exit(server.serve(proc)? as _);
}
