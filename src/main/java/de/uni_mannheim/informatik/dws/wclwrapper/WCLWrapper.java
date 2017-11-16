/*
 * Copyright 2017 Dmitry Ustalov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.uni_mannheim.informatik.dws.wclwrapper;

import it.uniroma1.lcl.jlt.util.Language;
import it.uniroma1.lcl.wcl.classifiers.lattice.TripleLatticeClassifier;
import it.uniroma1.lcl.wcl.classifiers.lattice.WCLClassifier;
import it.uniroma1.lcl.wcl.data.dataset.AnnotatedDataset;
import it.uniroma1.lcl.wcl.data.dataset.Dataset;
import it.uniroma1.lcl.wcl.data.sentence.Sentence;
import it.uniroma1.lcl.wcl.data.sentence.SentenceAnnotation;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This is a simple wrapper for the Word-Class Lattices Java API.
 * It classifies whether the given sentence is a definition of the given term.
 * <p>
 * The STDIN format is word&le;TAB&ge;sentence per line.
 * The STDOUT format is word&le;TAB&ge;boolean per line.
 *
 * @author dustalov
 */
public class WCLWrapper {
    final static Map<String, Language> LANGUAGES = EnumSet.allOf(Language.class).stream().collect(Collectors.toMap(l -> l.toString().toLowerCase(), Function.identity()));

    public static void main(String args[]) throws IOException {
        final Options options = new Options();
        options.addRequiredOption("l", "language", true, "Target language, e.g., en");
        options.addRequiredOption("t", "training", true, "Training set, e.g., data/training/wiki_good.EN.html");

        CommandLine cmd = null;

        try {
            cmd = new DefaultParser().parse(options, args);
        } catch (ParseException ex) {
            System.err.println(ex.getMessage());
            final HelpFormatter formatter = new HelpFormatter();
            formatter.setWidth(-1);
            formatter.printHelp("java -jar wcl-wrapper.jar -l en -t data/training/wiki_good.EN.html", options);
            System.exit(1);
        }

        final Language language = LANGUAGES.get(cmd.getOptionValue("language").toLowerCase());
        final Dataset dataset = new AnnotatedDataset(cmd.getOptionValue("training"), language);

        final WCLClassifier classifier = new TripleLatticeClassifier(language);
        classifier.train(dataset);

        final Scanner input = new Scanner(System.in);

        while (input.hasNextLine()) {
            final String line = input.nextLine();
            final String[] split = line.split("\t", 2);

            if (split.length != 2) {
                System.err.printf("Bad input: %s\n", line);
                continue;
            }

            final Sentence sentence = Sentence.createFromString(split[0], split[1], language);
            final SentenceAnnotation sa = classifier.test(sentence);

            System.out.printf("%s\t%s\n", split[0], Boolean.toString(sa.isDefinition()));
        }
    }
}
