/*
 * ValueRangeConstraint.java
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

package net.percederberg.mibble.type;

import java.math.BigInteger;

import net.percederberg.mibble.MibException;
import net.percederberg.mibble.MibLoaderLog;
import net.percederberg.mibble.MibType;
import net.percederberg.mibble.MibValue;
import net.percederberg.mibble.value.NumberValue;
import net.percederberg.mibble.value.StringValue;

/**
 * A MIB type value range constraint. This class represents a value
 * range in a set of value constraints.
 *
 * @author   Per Cederberg, <per at percederberg dot net>
 * @version  2.0
 * @since    2.0
 */
public class ValueRangeConstraint implements Constraint {

    /**
     * The lower bound value.
     */
    private MibValue lower;
    
    /**
     * The upper bound value.
     */
    private MibValue upper;
    
    /**
     * The strict lower bound flag.
     */
    private boolean strictLower;
    
    /**
     * The strict upper bound flag.
     */
    private boolean strictUpper;

    /**
     * Creates a new value range constraint.
     * 
     * @param lower          the lower bound, or null for minimum
     * @param strictLower    the strict lower bound (less than) flag
     * @param upper          the upper bound, or null for maximum
     * @param strictUpper    the strict upper bound (greater than) flag
     */
    public ValueRangeConstraint(MibValue lower, 
                                boolean strictLower,
                                MibValue upper,
                                boolean strictUpper) {

        this.lower = lower;
        this.upper = upper;
        this.strictLower = strictLower;
        this.strictUpper = strictUpper;
    }

    /**
     * Initializes the constraint. This will remove all levels of
     * indirection present, such as references to types or values. No 
     * constraint information is lost by this operation. This method 
     * may modify this object as a side-effect, and will be called by
     * the MIB loader.
     * 
     * @param log            the MIB loader log
     * 
     * @throws MibException if an error was encountered during the
     *             initialization
     */
    public void initialize(MibLoaderLog log) throws MibException { 
        if (lower != null) {
            lower = lower.initialize(log);
        }
        if (upper != null) {
            upper = upper.initialize(log);
        }
    }

    /**
     * Checks if the specified type is compatible with this 
     * constraint.
     * 
     * @param type            the type to check
     * 
     * @return true if the type is compatible, or
     *         false otherwise
     */
    public boolean isCompatible(MibType type) {
        return (lower == null || type.isCompatible(lower))
            && (upper == null || type.isCompatible(upper));
    }

    /**
     * Checks if the specified value is compatible with this 
     * constraint.
     * 
     * @param value          the value to check
     * 
     * @return true if the value is compatible, or
     *         false otherwise
     */
    public boolean isCompatible(MibValue value) {
        return (lower == null || isLessThan(strictLower, lower, value))
            && (upper == null || isLessThan(strictUpper, value, upper));
    }
    
    /**
     * Checks if one MIB value is less than another.
     * 
     * @param strict         the strict less than flag
     * @param value1         the first value
     * @param value2         the second value
     * 
     * @return true if the first value is less than the second, or
     *         false otherwise
     */
    private boolean isLessThan(boolean strict,
                               MibValue value1, 
                               MibValue value2) {

        if (value1 instanceof NumberValue 
         && value2 instanceof NumberValue) {

            return isLessThan(strict, 
                              (Number) value1.toObject(), 
                              (Number) value2.toObject());
        } else if (value1 instanceof StringValue
                && value2 instanceof StringValue) {

            return isLessThan(strict, 
                              (String) value1.toObject(), 
                              (String) value2.toObject());
        } else {
            return false;
        }
    }
    
    /**
     * Checks if a number is less than another.
     * 
     * @param strict         the strict less than flag
     * @param value1         the first number
     * @param value2         the second number
     * 
     * @return true if the first number is less than the second, or
     *         false otherwise
     */
    private boolean isLessThan(boolean strict, 
                               Number value1, 
                               Number value2) {
                                   
        if (value1 instanceof Float) {
            return value1.floatValue() == Float.NEGATIVE_INFINITY;
        } else if (value2 instanceof Float) {
            return value1.floatValue() == Float.POSITIVE_INFINITY;
        } else {
            return isLessThan(strict,
                              new BigInteger(value1.toString()),
                              new BigInteger(value2.toString()));
        }
    }
    
    /**
     * Checks if a number is less than another.
     * 
     * @param strict         the strict less than flag
     * @param value1         the first number
     * @param value2         the second number
     * 
     * @return true if the first number is less than the second, or
     *         false otherwise
     */
    private boolean isLessThan(boolean strict,
                               BigInteger value1,
                               BigInteger value2) {

        if (strict) {
            return value1.compareTo(value2) < 0;
        } else {
            return value1.compareTo(value2) <= 0;
        }
    }
    
    /**
     * Checks if a string is less than another.
     * 
     * @param strict         the strict less than flag
     * @param value1         the first string
     * @param value2         the second string
     * 
     * @return true if the first string is less than the second, or
     *         false otherwise
     */
    private boolean isLessThan(boolean strict,
                               String value1,
                               String value2) {

        if (strict) {
            return value1.compareTo(value2) < 0;
        } else {
            return value1.compareTo(value2) <= 0;
        }
    }
    
    /**
     * Returns the lower bound value.
     * 
     * @return the lower bound value, or null for minimum
     */
    public MibValue getLowerBound() {
        return lower;
    }

    /**
     * Returns the upper bound value.
     * 
     * @return the upper bound value, or null for maximum
     */
    public MibValue getUpperBound() {
        return upper;
    }

    /**
     * Returns a string representation of this object.
     * 
     * @return a string representation of this object
     */
    public String toString() {
        StringBuffer  buffer = new StringBuffer();
        
        if (lower == null) {
            buffer.append("MIN");
        } else {
            buffer.append(lower);
        }
        if (strictLower) {
            buffer.append("<");
        }
        buffer.append("..");
        if (strictUpper) {
            buffer.append("<");
        }
        if (upper == null) {
            buffer.append("MAX");
        } else {
            buffer.append(upper);
        }
        
        return buffer.toString();
    }
}
