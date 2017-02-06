# A note on Integration testing

The extractDeps method logs the xml content of the conflicting objects.
However, the reported value might NOT be the actual one. In fact, the xml is generated
at the moment the first conflict triggers. This also means that, if the current tests
writes a new value, the new value will not appear in the console output 