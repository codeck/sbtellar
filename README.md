sbtellar
=========

a sbt plugin to participate stellar-core network (eventually, a scala fork of stellar-core)

```
Concept mapping:
user => stellar account
group => federation name
host => stellar-core peer
file => trustline/contract/... advancedt ransactions
```

Deployment should be specific before each run.
Deployment consists of Network(XLM, KLM), Stage(Single/Standalone, Test, Live) and Role(Watcher, Validator).

after Deployment determined, run level should specified by `init`

- 0: stop
- 1: restart (use current db)
- 2: reset to minimal (no full history)
- 3: reinit (initdb)
- 4: full reset (create new dir)

### Supported Commands:

##### \>newkey

Generate new random address

##### \>who

List all the address and corresponding secure key in su history

---

# TODO

* clear

Clear screen

* whoami

Show current active address

* id

Show current address and info

* su <newSecureKey>

use a new key

* finger <otheracct>

* send <otheracct> <amount>

* convertid <sxxxx|Gxxxx|SXXXX>

sxxxx => (gxxxx, GXXXX, SXXXX)
Gxxxx => (gxxxx)
SXXXX => (gxxxx, GXXXX, sxxxxx)

* dmesg

show stellar-core logs

* init

start local stellar-core instance

* set

show/set(TODO) current state

* reboot

restart local stellar-core instance

* rsh <peerip:port>

Use rpc of this ip:port

* ping <peerip|nodeid>

Query peer information

* ss|netstat

Show peers

* uptime

Current ledger status, node states

* hostname

peer id and validator id of current node

* uname

current host system infomation

* ls <credits|votes|signers|orders|...>

list...

* ed <credits|votes|signers|orders|...>

edit...

TBD: touch/top/ps/whois/host..?
TBD: gpasswd/newgrp/sg... for federation?
TBD: init, server, believe, info, tx, send, dumpxdr, help
