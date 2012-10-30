/*
A Java-based HTTP stub server

Copyright (C) 2012 Alexander Zagniotov, Isa Goksu and Eric Mrak

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package by.stub.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import by.stub.utils.StringUtils;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public final class CommandLineIntepreter {

   private static CommandLine line = null;
   private static final CommandLineParser POSIX_PARSER = new PosixParser();
   private static final Options OPTIONS = new Options();

   public static final String OPTION_ADDRESS = "location";
   public static final String OPTION_CLIENTPORT = "stubs";
   public static final String OPTION_ADMINPORT = "admin";
   public static final String OPTION_CONFIG = "data";
   public static final String OPTION_KEYSTORE = "keystore";
   public static final String OPTION_KEYPASS = "password";
   public static final String OPTION_MUTE = "mute";

   private static final String[] ALL_OPTIONS = {OPTION_ADDRESS, OPTION_CLIENTPORT, OPTION_ADMINPORT, OPTION_CONFIG, OPTION_KEYSTORE, OPTION_KEYPASS};

   public static final String OPTION_HELP = "help";

   static {
      OPTIONS.addOption("l", OPTION_ADDRESS, true, "Hostname at which to bind stubby.");
      OPTIONS.addOption("s", OPTION_CLIENTPORT, true, "Port for stub portal. Defaults to 8882.");
      OPTIONS.addOption("a", OPTION_ADMINPORT, true, "Port for admin portal. Defaults to 8889.");
      OPTIONS.addOption("d", OPTION_CONFIG, true, "Data file to pre-load endpoints. YAML expected.");
      OPTIONS.addOption("k", OPTION_KEYSTORE, true, "Keystore file for enabling SSL.");
      OPTIONS.addOption("p", OPTION_KEYPASS, true, "Password for the provided keystore file.");
      OPTIONS.addOption("h", OPTION_HELP, false, "This help text.");
      OPTIONS.addOption("m", OPTION_MUTE, false, "Prevent stubby from printing to the console");
   }

   private CommandLineIntepreter() {

   }

   public static void parseCommandLine(final String[] args) throws ParseException {
      line = POSIX_PARSER.parse(OPTIONS, args);
   }

   public static String getCurrentJarLocation(final Class theclass) {
      final URL location = theclass.getProtectionDomain().getCodeSource().getLocation();
      final String jar = new File(location.getFile()).getName();

      if (StringUtils.toLower(jar).endsWith(".jar")) {
         return jar;
      }

      return "stubby4j-x.x.x-SNAPSHOT.jar";
   }

   public static boolean isMute() {
      return line.hasOption(OPTION_MUTE);
   }

   public static boolean isSslRequested() {
      return line.hasOption(OPTION_KEYSTORE);
   }

   public static boolean isYamlProvided() {
      return line.hasOption(OPTION_CONFIG);
   }

   public static boolean isHelp() {
      return line.hasOption(OPTION_HELP);
   }

   public static void printHelp(final Class theclass) {
      final HelpFormatter formatter = new HelpFormatter();
      final String command = String.format("%sjava -jar %s", "\n", getCurrentJarLocation(theclass));
      formatter.printHelp(command, OPTIONS, true);
   }

   public static Map<String, String> getCommandlineParams() {

      final Map<String, String> params = new HashMap<String, String>();
      for (final String option : ALL_OPTIONS) {
         if (line.hasOption(option)) {
            params.put(option, line.getOptionValue(option));
         }
      }
      return params;
   }
}