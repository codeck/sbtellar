sbtellar
=========

a sbt plugin to participate stellar-core network (eventually, a scala fork of stellar-core)

```
Concept mapping:
user => stellar account
host => stellar-core peer
file => trustline/contract/... advancedt ransactions
```
Supported Commands:

* newkey

Generate new random address

* id

List all the address and correspoding secure key in su history

* whoami

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

TODO: /w|who/touch/top/ps/whois/host/..?
TODO: init, server, believe, info, tx, send, dumpxdr, help
