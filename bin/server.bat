REM  -----------------------------------------------------------
REM  PM Run Server (Same as old PmServer/server.bat)
REM  Steve Quirolgico
REM  06/07/08
REM  -----------------------------------------------------------


REM ------------------------------------------------------------
REM     MODIFY THE FOLLOWING PATHS FOR YOUR ENVIRONMENT!
REM ------------------------------------------------------------



set PM_VERSION=1.6

set PM_ROOT=C:\PM

set MY_KEYSTORE=serverKeystore

set MY_TRUSTSTORE=serverTruststore

set ENGINE_PORT=8080

set JAVA_JRE=%JAVA_HOME%

TITLE Policy Machine Server


REM ------------------------------------------------------------
REM       DO NOT MODIFY ANYTHING BELOW THIS LINE!
REM ------------------------------------------------------------



set KEYSTORES=%PM_ROOT%\keystores

set MY_KEYSTORE_PATH=%KEYSTORES%\%MY_KEYSTORE%

set MY_TRUSTSTORE_PATH=%KEYSTORES%\%MY_TRUSTSTORE%

set JAVA_BIN=%JAVA_JRE%\bin

set JAVA_LIB=%JAVA_JRE%\lib

set JAVA_JARS=%JAVA_LIB%\rt.jar;%JAVA_LIB%\jsse.jar

set PM_SERVER_JAR=%PM_ROOT%\dist\pm-server-%PM_VERSION%.jar

set PM_DATASTORE=SQL

set CLASSPATH=%PM_SERVER_JAR%;%PM_ROOT%\dist\pm-commons-%PM_VERSION%.jar;%PM_ROOT%\lib\activation-1.1.jar;%PM_ROOT%\lib\aopalliance-1.0.jar;%PM_ROOT%\lib\asm-3.1.jar;%PM_ROOT%\lib\bcmail-jdk15-1.44.jar;%PM_ROOT%\lib\bcprov-jdk15-1.44.jar;%PM_ROOT%\lib\cglib-2.2.1-v20090111.jar;%PM_ROOT%\lib\colorchooser-1.0.jar;%PM_ROOT%\lib\commons-logging-1.1.1.jar;%PM_ROOT%\lib\fontbox-1.6.0.jar;%PM_ROOT%\lib\guava-r09.jar;%PM_ROOT%\lib\guice-3.0.jar;%PM_ROOT%\lib\icu4j-3.8.jar;%PM_ROOT%\lib\jarjar-1.0.jar;%PM_ROOT%\lib\javax.inject-1.jar;%PM_ROOT%\lib\javax.mail-1.4.4.jar;%PM_ROOT%\lib\jempbox-1.6.0.jar;%PM_ROOT%\lib\jfontchooser-1.0.5-pm.jar;%PM_ROOT%\lib\jna-3.2.7-pm-platform.jar;%PM_ROOT%\lib\jna-3.2.7-pm.jar;%PM_ROOT%\lib\jsr305-1.3.7.jar;%PM_ROOT%\lib\miglayout-3.7.3.1-swing.jar;%PM_ROOT%\lib\pdfbox-1.6.0.jar;%PM_ROOT%\lib\wrapper-3.2.3.jar;%PM_ROOT%\lib\wrapper.jar;%PM_ROOT%\lib\mysql-connector-java-5.1.31-bin.jar;%PM_ROOT%\lib\mchange-commons-java-0.2.11.jar;%PM_ROOT%\lib\c3p0-0.9.5.2.jar

set WAIT_FOR_DEBUGGER=n
set DEBUG_PORT=8003
set SERVER_MODE=y

set DEBUG_ARGS=-Xdebug -agentlib:jdwp=transport=dt_socket,suspend=%WAIT_FOR_DEBUGGER%,server=%SERVER_MODE%,address=%DEBUG_PORT%

echo Debug ARGS: %DEBUG_ARGS%

"%JAVA_BIN%\java" -cp "%CLASSPATH%" -Djavax.net.ssl.keyStore=%MY_KEYSTORE_PATH% -Djavax.net.ssl.trustStore=%MY_TRUSTSTORE_PATH% gov.nist.csd.pm.server.PmEngine -engineport %ENGINE_PORT% -datastore %PM_DATASTORE% -debug


