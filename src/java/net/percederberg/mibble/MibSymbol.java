/*
 * MibSymbol.java
 *
 * This work is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 2 of the License,
 * or (at your option) any later version.
 *
 * This work is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 *
 * As a special exception, the copyright holders of this library give
 * you permission to link this library with independent modules to
 * produce an executable, regardless of the license terms of these
 * independent modules, and to copy and distribute the resulting
 * executable under terms of your choice, provided that you also meet,
 * for each linked independent module, the terms and conditions of the
 * license of that module. An independent module is a module which is
 * not derived from or based on this library. If you modify this
 * library, you may extend this exception to your version of the
 * library, but you are not obligated to do so. If you do not wish to
 * do so, delete this exception statement from your version.
 *
 * Copyright (c) 2003 Per Cederberg. All rights reserved.
 */

package net.percederberg.mibble;

/**
 * A MIB symbol. This is the base class for all symbols in a MIB file.
 * Each symbol is typically identified by it's name, which must be 
 * unique within the MIB file. All symbols also have a data type.
 *
 * @author   Per Cederberg, <per at percederberg dot net>
 * @version  2.0
 * @since    2.0
 */
public abstract class MibSymbol {

    /**
     * The symbol location.
     */
    private FileLocation location;

    /**
     * The symbol name.
     */
    private String name;
    
    /**
     * Creates a new symbol with the specified name.
     * 
     * @param name           the symbol name
     * @param location       the symbol location
     */
    public MibSymbol(FileLocation location, String name) {
        this.location = location;
        this.name = name;
    }

    /**
     * Initializes the MIB symbol. This will remove all levels of
     * indirection present, such as references to types or values. No 
     * information is lost by this operation. This method may modify
     * this object as a side-effect, and will be called by the MIB 
     * loader.
     * 
     * @param log            the MIB loader log
     * 
     * @throws MibException if an error was encountered during the
     *             initialization
     */
    public abstract void initialize(MibLoaderLog log) throws MibException;

    /**
     * Returns the file location.
     * 
     * @return the file location
     */
    public FileLocation getLocation() {
        return location;
    }

    /**
     * Returns the symbol name.
     * 
     * @return the symbol name
     */
    public String getName() {
        return name;
    }
}
