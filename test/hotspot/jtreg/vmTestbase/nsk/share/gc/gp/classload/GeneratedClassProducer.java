/*
 * Copyright (c) 2007, 2018, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package nsk.share.gc.gp.classload;

import nsk.share.gc.gp.GarbageProducer;
import nsk.share.classload.GeneratingClassLoader;
import nsk.share.classload.GeneratingClassLoader;
import nsk.share.gc.gp.GarbageUtils;
import nsk.share.test.LocalRandom;

/**
 * Garbage producer that creates classes loaded with GeneratingClassLoader.
 *
 * Note: this class is not thread-safe.
 */
public class GeneratedClassProducer implements GarbageProducer<Class> {
        private int number;
        private String className;
        private StringBuilder sb = new StringBuilder();
        private int minPerClassLoader = 50;
        private int maxPerClassLoader = 150;
        private int count;
        private GeneratingClassLoader loader = new GeneratingClassLoader();

        public GeneratedClassProducer() {
                this(GeneratingClassLoader.DEFAULT_CLASSNAME);
        }

        public GeneratedClassProducer(String className) {
                this.className = className;
        }

        private String getNewName() {
                sb.delete(0, sb.length());
                sb.append("Class");
                sb.append(number);
                int n = loader.getNameLength() - sb.length();
                for (int i = 0; i < n; ++i)
                        sb.append('_');
                return sb.toString();
        }

        public Class create(long memory) {
                try {
                        if (number++ > maxPerClassLoader || loader == null) {
                                loader = new GeneratingClassLoader(className);
                                count = LocalRandom.nextInt(minPerClassLoader, maxPerClassLoader);
                                number = 0;
                        }
                        return loader.loadClass(getNewName());
                } catch (ClassNotFoundException e) {
                        throw convertException(e);
                }
        }

        public void validate(Class cl) {
        }

        protected RuntimeException convertException(Exception e) {
                return new RuntimeException(e);
        }
}
