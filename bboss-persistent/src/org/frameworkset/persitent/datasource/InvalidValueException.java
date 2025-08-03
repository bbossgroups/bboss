package org.frameworkset.persitent.datasource;
/**
 * Copyright 2025 bboss
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.sql.SQLException;

/**
 * @author biaoping.yin
 * @Date 2025/8/2
 */
public class InvalidValueException extends SQLException {
    /**
     * Constructs a {@code SQLException} object with a given
     * {@code reason}, {@code SQLState}  and
     * {@code vendorCode}.
     * <p>
     * The {@code cause} is not initialized, and may subsequently be
     * initialized by a call to the
     * {@link Throwable#initCause(Throwable)} method.
     *
     * @param reason     a description of the exception
     * @param SQLState   an XOPEN or SQL:2003 code identifying the exception
     * @param vendorCode a database vendor-specific exception code
     */
    public InvalidValueException(String reason, String SQLState, int vendorCode) {
        super(reason, SQLState, vendorCode);
    }

    /**
     * Constructs a {@code SQLException} object with a given
     * {@code reason} and {@code SQLState}.
     * <p>
     * The {@code cause} is not initialized, and may subsequently be
     * initialized by a call to the
     * {@link Throwable#initCause(Throwable)} method. The vendor code
     * is initialized to 0.
     *
     * @param reason   a description of the exception
     * @param SQLState an XOPEN or SQL:2003 code identifying the exception
     */
    public InvalidValueException(String reason, String SQLState) {
        super(reason, SQLState);
    }

    /**
     * Constructs a {@code SQLException} object with a given
     * {@code reason}. The  {@code SQLState}  is initialized to
     * {@code null} and the vendor code is initialized to 0.
     * <p>
     * The {@code cause} is not initialized, and may subsequently be
     * initialized by a call to the
     * {@link Throwable#initCause(Throwable)} method.
     *
     * @param reason a description of the exception
     */
    public InvalidValueException(String reason) {
        super(reason);
    }

    /**
     * Constructs a {@code SQLException} object.
     * The {@code reason}, {@code SQLState} are initialized
     * to {@code null} and the vendor code is initialized to 0.
     * <p>
     * The {@code cause} is not initialized, and may subsequently be
     * initialized by a call to the
     * {@link Throwable#initCause(Throwable)} method.
     */
    public InvalidValueException() {
    }

    /**
     * Constructs a {@code SQLException} object with a given
     * {@code cause}.
     * The {@code SQLState} is initialized
     * to {@code null} and the vendor code is initialized to 0.
     * The {@code reason}  is initialized to {@code null} if
     * {@code cause==null} or to {@code cause.toString()} if
     * {@code cause!=null}.
     *
     * @param cause the underlying reason for this {@code SQLException}
     *              (which is saved for later retrieval by the {@code getCause()} method);
     *              may be null indicating the cause is non-existent or unknown.
     * @since 1.6
     */
    public InvalidValueException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a {@code SQLException} object with a given
     * {@code reason} and  {@code cause}.
     * The {@code SQLState} is  initialized to {@code null}
     * and the vendor code is initialized to 0.
     *
     * @param reason a description of the exception.
     * @param cause  the underlying reason for this {@code SQLException}
     *               (which is saved for later retrieval by the {@code getCause()} method);
     *               may be null indicating the cause is non-existent or unknown.
     * @since 1.6
     */
    public InvalidValueException(String reason, Throwable cause) {
        super(reason, cause);
    }

    /**
     * Constructs a {@code SQLException} object with a given
     * {@code reason}, {@code SQLState} and  {@code cause}.
     * The vendor code is initialized to 0.
     *
     * @param reason   a description of the exception.
     * @param sqlState an XOPEN or SQL:2003 code identifying the exception
     * @param cause    the underlying reason for this {@code SQLException}
     *                 (which is saved for later retrieval by the
     *                 {@code getCause()} method); may be null indicating
     *                 the cause is non-existent or unknown.
     * @since 1.6
     */
    public InvalidValueException(String reason, String sqlState, Throwable cause) {
        super(reason, sqlState, cause);
    }

    /**
     * Constructs a {@code SQLException} object with a given
     * {@code reason}, {@code SQLState}, {@code vendorCode}
     * and  {@code cause}.
     *
     * @param reason     a description of the exception
     * @param sqlState   an XOPEN or SQL:2003 code identifying the exception
     * @param vendorCode a database vendor-specific exception code
     * @param cause      the underlying reason for this {@code SQLException}
     *                   (which is saved for later retrieval by the {@code getCause()} method);
     *                   may be null indicating the cause is non-existent or unknown.
     * @since 1.6
     */
    public InvalidValueException(String reason, String sqlState, int vendorCode, Throwable cause) {
        super(reason, sqlState, vendorCode, cause);
    }
}
