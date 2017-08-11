package org.apache.maven.doxia.module.twiki.parser;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.sink.SinkSymbol;

import java.util.HashMap;
import java.util.Map;

/**
 * Block that holds plain text
 *
 * @author Juan F. Codagnone
 * @version $Id$
 */
class MacroBlock
    implements Block
{
    /**
     * the text
     */
    private final String name;
    private final String arguments;

    /**
     * Creates the MacroBlock.
     *
     * @param name the name. can't ben <code>null</code>
     * @param arguments arguments. can ben <code>null</code>
     * @throws IllegalArgumentException if parameters are not in the domain
     */
    MacroBlock(final String name, String arguments )
    {
        if ( name == null )
        {
            throw new IllegalArgumentException( "argument can't be null" );
        }

        this.name = name;
        this.arguments = arguments;
    }

    /** {@inheritDoc} */
    public final void traverse( final Sink sink )
    {
        if (arguments == null || name.toUpperCase().equals("ICON")) {
            if (name.equals("BR")) {
                sink.lineBreak();
                return;
            }
            Map<String, SinkSymbol> symbolmap = new HashMap<String, SinkSymbol>() { {
                put("NO", SinkSymbol.NO);
                put("Y", SinkSymbol.OK);
                put("ICON{INFO}", SinkSymbol.INFORMATION);
                put("ICON{ALERT}", SinkSymbol.EXCLAMATION);
                put("ICON{ADD}", SinkSymbol.PLUS);
                put("ICON{REMOVE}", SinkSymbol.MINUS);
            }};
            SinkSymbol result = symbolmap.get(name.toUpperCase());
            if (result == null && name.equals("ICON") && arguments != null) {
                result = symbolmap.get(name.toUpperCase() + arguments.toUpperCase());
            }
            if (result != null) {
                sink.symbol(result);
                return;
            }
        }

        sink.body_();
        sink.verbatim_();
        sink.text( "%" + name + (arguments != null ? arguments : "") + "%\n" );
        sink.verbatim_();
    }

    /** {@inheritDoc} */
    public final String toString()
    {
        return getClass().getName() + ": [" + name.replaceAll( "\n", "\\n" ) + "]";
    }

    /**
     * Returns the text.
     *
     * @return <code>String</code> with the text.
     */
    final String getName()
    {
        return name;
    }
    final String getArguments()
    {
        return arguments;
    }

    /** {@inheritDoc} */
    public final boolean equals( final Object obj )
    {
        boolean ret = false;

        if ( obj == this )
        {
            ret = true;
        }
        else if ( obj instanceof MacroBlock)
        {
            final MacroBlock macroBlock = (MacroBlock) obj;
            ret = name.equals( macroBlock.name ) && arguments == macroBlock.arguments || arguments != null && arguments.equals(macroBlock.arguments);
        }

        return ret;
    }

    /** {@inheritDoc} */
    public final int hashCode()
    {
        return name.hashCode();
    }
}
