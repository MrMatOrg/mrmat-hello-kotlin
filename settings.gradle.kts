
rootProject.name = "mrmat-hello-kotlin"

include(":versioning")
project(":versioning").projectDir = file("apps/versioning")

include(":helloworld-spring")
project(":helloworld-spring").projectDir = file("apps/helloworld-spring")

include(":deployment:helm")
project(":deployment:helm").projectDir = file("deployment/helm")

include(":deployment:rpm")
project(":deployment:rpm").projectDir = file("deployment/rpm")

include(":deployment:ansible")
project(":deployment:ansible").projectDir = file("deployment/ansible")
