/*
 * The MIT License
 *
 * Copyright 2014 Daniel Kec <daniel at kecovi.cz>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package cz.kec.nb.ejbutils;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel Kec <daniel at kecovi.cz>
 */
public class AnnotationParser {
    HashMap<String,String> map = new HashMap<String,String>();
    public AnnotationParser(String annParams) {
       // LOG.log(Level.INFO, "Parsing annotation params {0}", annParams);
        if(annParams==null)return;
        annParams = annParams.replaceAll("\\(", "").replaceAll("\\)", "");
        // name="ss"
        // description="bb"
        String[] params = annParams.split(",");
        for (String param : params) {
            String[] par = param.split("=");
            map.put(par[0].trim(), par[1].replaceAll("\"","").replaceAll("'", "").trim());
        }
    }
    private static final Logger LOG = Logger.getLogger(AnnotationParser.class.getName());
    
    public String getValue(String key){
       // LOG.log(Level.INFO,"Getting annotation param {0} with value {1}",new Object[]{key,this.map.get(key)});
        return this.map.get(key);
    }
    
}
