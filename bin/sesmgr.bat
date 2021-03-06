REM  -----------------------------------------------------------
REM  PM Run User (same as old Pmuser/sesmgrdbg.bat)
REM  Steve Quirolgico
REM  06/07/08
REM  -----------------------------------------------------------


REM ------------------------------------------------------------
REM     MODIFY THE FOLLOWING PATHS FOR YOUR ENVIRONMENT!
REM ------------------------------------------------------------



set PM_VERSION=1.6

set PM_ROOT=C:\PM

set MY_KEYSTORE=%computername%Keystore

set MY_KEYSTORE_PASSWORD=aaaaaa

set MY_TRUSTSTORE=clientTruststore

set SIM_PORT=8081

set EX_PORT=8082

set JAVA_JRE=%JAVA_HOME%

TITLE Policy Machine Session Manager


REM ------------------------------------------------------------
REM       DO NOT MODIFY ANYTHING BELOW THIS LINE!
REM ------------------------------------------------------------



set KEYSTORES=%PM_ROOT%\keystores

set MY_KEYSTORE_PATH=%KEYSTORES%\%MY_KEYSTORE%

set MY_TRUSTSTORE_PATH=%KEYSTORES%\%MY_TRUSTSTORE%

set JAVA_BIN=%JAVA_JRE%\bin

set JAVA_LIB=%JAVA_JRE%\lib

set CLASSPATH=%PM_ROOT%\dist\pm-user-%PM_VERSION%.jar;%PM_ROOT%\dist\pm-exporter-%PM_VERSION%.jar;%PM_ROOT%\dist\pm-commons-%PM_VERSION%.jar;%PM_ROOT%\lib\activation-1.1.jar;%PM_ROOT%\lib\aopalliance-1.0.jar;%PM_ROOT%\lib\asm-3.1.jar;%PM_ROOT%\lib\bcmail-jdk15-1.44.jar;%PM_ROOT%\lib\bcprov-jdk15-1.44.jar;%PM_ROOT%\lib\cglib-2.2.1-v20090111.jar;%PM_ROOT%\lib\colorchooser-1.0.jar;%PM_ROOT%\lib\commons-logging-1.1.1.jar;%PM_ROOT%\lib\fontbox-1.6.0.jar;%PM_ROOT%\lib\guava-r09.jar;%PM_ROOT%\lib\guice-3.0.jar;%PM_ROOT%\lib\icu4j-3.8.jar;%PM_ROOT%\lib\jarjar-1.0.jar;%PM_ROOT%\lib\javax.inject-1.jar;%PM_ROOT%\lib\javax.mail-1.4.4.jar;%PM_ROOT%\lib\jempbox-1.6.0.jar;%PM_ROOT%\lib\jfontchooser-1.0.5-pm.jar;%PM_ROOT%\lib\jna-3.2.7-pm-platform.jar;%PM_ROOT%\lib\jna-3.2.7-pm.jar;%PM_ROOT%\lib\jsr305-1.3.7.jar;%PM_ROOT%\lib\miglayout-3.7.3.1-swing.jar;%PM_ROOT%\lib\pdfbox-1.6.0.jar;%PM_ROOT%\lib\wrapper-3.2.3.jar;%PM_ROOT%\lib\wrapper.jar;%PM_ROOT%\lib\mysql-connector-java-5.1.31-bin.jar

"%JAVA_BIN%\java" -cp "%CLASSPATH%" -Djavax.net.ssl.keyStore=%MY_KEYSTORE_PATH% -Djavax.net.ssl.keyStorePassword=%MY_KEYSTORE_PASSWORD% -Djavax.net.ssl.trustStore=%MY_TRUSTSTORE_PATH% gov.nist.csd.pm.user.SessionManager -simport %SIM_PORT% -export %EX_PORT% -debug
