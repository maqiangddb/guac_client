/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is guacamole-auth-mysql.
 *
 * The Initial Developer of the Original Code is
 * James Muehlner.
 * Portions created by the Initial Developer are Copyright (C) 2010
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 *
 * ***** END LICENSE BLOCK ***** */
package net.sourceforge.guacamole.net.auth.mysql;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.glyptodon.guacamole.GuacamoleException;
import org.glyptodon.guacamole.net.auth.AbstractUser;
import org.glyptodon.guacamole.net.auth.User;
import org.glyptodon.guacamole.net.auth.permission.Permission;

/**
 * A MySQL based implementation of the User object.
 * @author James Muehlner
 */
public class MySQLUser extends AbstractUser {

    /**
     * The ID of this user in the database, if any.
     */
    private Integer userID;

    /**
     * The set of current permissions a user has.
     */
    private Set<Permission> permissions = new HashSet<Permission>();

    /**
     * Any newly added permissions that have yet to be committed.
     */
    private Set<Permission> newPermissions = new HashSet<Permission>();

    /**
     * Any newly deleted permissions that have yet to be deleted.
     */
    private Set<Permission> removedPermissions = new HashSet<Permission>();

    /**
     * Creates a new, empty MySQLUser.
     */
    public MySQLUser() {
    }

    /**
     * Initializes a new MySQLUser having the given username.
     *
     * @param name The name to assign to this MySQLUser.
     */
    public void init(String name) {
        init(null, name, null, Collections.EMPTY_SET);
    }

    /**
     * Initializes a new MySQLUser, copying all data from the given user
     * object.
     *
     * @param user The user object to copy.
     * @throws GuacamoleException If an error occurs while reading the user
     *                            data in the given object.
     */
    public void init(User user) throws GuacamoleException {
        init(null, user.getUsername(), user.getPassword(), user.getPermissions());
    }

    /**
     * Initializes a new MySQLUser initialized from the given data from the
     * database.
     *
     * @param userID The ID of the user in the database, if any.
     * @param username The username of this user.
     * @param password The password to assign to this user.
     * @param permissions The permissions to assign to this user, as
     *                    retrieved from the database.
     */
    public void init(Integer userID, String username, String password,
            Set<Permission> permissions) {
        this.userID = userID;
        setUsername(username);
        setPassword(password);
        this.permissions.addAll(permissions);
    }

    /**
     * Get the current set of permissions this user has.
     * @return the current set of permissions.
     */
    public Set<Permission> getCurrentPermissions() {
        return permissions;
    }

    /**
     * Get any new permissions that have yet to be inserted.
     * @return the new set of permissions.
     */
    public Set<Permission> getNewPermissions() {
        return newPermissions;
    }

    /**
     * Get any permissions that have not yet been deleted.
     * @return the permissions that need to be deleted.
     */
    public Set<Permission> getRemovedPermissions() {
        return removedPermissions;
    }

    /**
     * Reset the new and removed permission sets after they are
     * no longer needed.
     */
    public void resetPermissions() {
        newPermissions.clear();
        removedPermissions.clear();
    }

    /**
     * Returns the ID of this user in the database, if it exists.
     *
     * @return The ID of this user in the database, or null if this user
     *         was not retrieved from the database.
     */
    public Integer getUserID() {
        return userID;
    }

    /**
     * Sets the ID of this user to the given value.
     *
     * @param userID The ID to assign to this user.
     */
    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    @Override
    public Set<Permission> getPermissions() throws GuacamoleException {
        return Collections.unmodifiableSet(permissions);
    }

    @Override
    public boolean hasPermission(Permission permission) throws GuacamoleException {
        return permissions.contains(permission);
    }

    @Override
    public void addPermission(Permission permission) throws GuacamoleException {
        permissions.add(permission);
        newPermissions.add(permission);
        removedPermissions.remove(permission);
    }

    @Override
    public void removePermission(Permission permission) throws GuacamoleException {
        permissions.remove(permission);
        newPermissions.remove(permission);
        removedPermissions.add(permission);
    }

}
