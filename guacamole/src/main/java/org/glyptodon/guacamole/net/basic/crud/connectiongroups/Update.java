package org.glyptodon.guacamole.net.basic.crud.connectiongroups;

/*
 *  Guacamole - Clientless Remote Desktop
 *  Copyright (C) 2010  Michael Jumper
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.glyptodon.guacamole.GuacamoleException;
import org.glyptodon.guacamole.net.auth.ConnectionGroup;
import org.glyptodon.guacamole.net.auth.Directory;
import org.glyptodon.guacamole.net.auth.UserContext;
import org.glyptodon.guacamole.net.basic.AuthenticatingHttpServlet;

/**
 * Simple HttpServlet which handles connection group update.
 *
 * @author James Muehlner
 */
public class Update extends AuthenticatingHttpServlet {

    @Override
    protected void authenticatedService(
            UserContext context,
            HttpServletRequest request, HttpServletResponse response)
    throws GuacamoleException {

        // Get ID, name, and type
        String identifier = request.getParameter("id");
        String name       = request.getParameter("name");
        String type       = request.getParameter("type");

        // Attempt to get connection group directory
        Directory<String, ConnectionGroup> directory =
                context.getRootConnectionGroup().getConnectionGroupDirectory();

        // Create connection group skeleton
        ConnectionGroup connectionGroup = directory.get(identifier);
        connectionGroup.setName(name);
        
        if("balancing".equals(type))
            connectionGroup.setType(ConnectionGroup.Type.BALANCING);
        else if("organizational".equals(type))
            connectionGroup.setType(ConnectionGroup.Type.ORGANIZATIONAL);

        // Update connection group
        directory.update(connectionGroup);

    }

}

