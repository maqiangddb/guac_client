
package org.glyptodon.guacamole.net.auth.simple;

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
 * The Original Code is guacamole-auth.
 *
 * The Initial Developer of the Original Code is
 * Michael Jumper.
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

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.glyptodon.guacamole.GuacamoleException;
import org.glyptodon.guacamole.GuacamoleSecurityException;
import org.glyptodon.guacamole.net.auth.AbstractUser;
import org.glyptodon.guacamole.net.auth.ConnectionGroup;
import org.glyptodon.guacamole.net.auth.permission.ConnectionGroupPermission;
import org.glyptodon.guacamole.net.auth.permission.ConnectionPermission;
import org.glyptodon.guacamole.net.auth.permission.ObjectPermission;
import org.glyptodon.guacamole.net.auth.permission.Permission;
import org.glyptodon.guacamole.protocol.GuacamoleConfiguration;


/**
 * An extremely basic User implementation.
 *
 * @author Michael Jumper
 */
public class SimpleUser extends AbstractUser {

    /**
     * The set of all permissions available to this user.
     */
    private Set<Permission> permissions = new HashSet<Permission>();

    /**
     * Creates a completely uninitialized SimpleUser.
     */
    public SimpleUser() {
    }

    /**
     * Creates a new SimpleUser having the given username.
     *
     * @param username The username to assign to this SimpleUser.
     * @param configs All configurations this user has read access to.
     * @param groups All groups this user has read access to.
     */
    public SimpleUser(String username,
            Map<String, GuacamoleConfiguration> configs,
            Collection<ConnectionGroup> groups) {

        // Set username
        setUsername(username);

        // Add connection permissions
        for (String identifier : configs.keySet()) {

            // Create permission
            Permission permission = new ConnectionPermission(
                ObjectPermission.Type.READ,
                identifier
            );

            // Add to set
            permissions.add(permission);

        }

        // Add group permissions
        for (ConnectionGroup group : groups) {

            // Create permission
            Permission permission = new ConnectionGroupPermission(
                ObjectPermission.Type.READ,
                group.getIdentifier()
            );

            // Add to set
            permissions.add(permission);

        }

    }

    @Override
    public Set<Permission> getPermissions() throws GuacamoleException {
        return permissions;
    }

    @Override
    public boolean hasPermission(Permission permission) throws GuacamoleException {
        return permissions.contains(permission);
    }

    @Override
    public void addPermission(Permission permission) throws GuacamoleException {
        throw new GuacamoleSecurityException("Permission denied.");
    }

    @Override
    public void removePermission(Permission permission) throws GuacamoleException {
        throw new GuacamoleSecurityException("Permission denied.");
    }

}
