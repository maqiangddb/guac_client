package org.glyptodon.guacamole.net.basic.xml.user_mapping;

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

import org.glyptodon.guacamole.net.basic.auth.UserMapping;
import org.glyptodon.guacamole.net.basic.xml.TagHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * TagHandler for the "user-mapping" element.
 *
 * @author Mike Jumper
 */
public class UserMappingTagHandler implements TagHandler {

    /**
     * The UserMapping which will contain all data parsed by this tag handler.
     */
    private UserMapping user_mapping = new UserMapping();

    @Override
    public void init(Attributes attributes) throws SAXException {
        // Do nothing
    }

    @Override
    public TagHandler childElement(String localName) throws SAXException {

        // Start parsing of authorize tags, add to list of all authorizations
        if (localName.equals("authorize"))
            return new AuthorizeTagHandler(user_mapping);

        return null;

    }

    @Override
    public void complete(String textContent) throws SAXException {
        // Do nothing
    }

    /**
     * Returns a user mapping containing all authorizations and configurations
     * parsed so far. This user mapping will be backed by the data being parsed,
     * thus any additional authorizations or configurations will be available
     * in the object returned by this function even after this function has
     * returned, once the data corresponding to those authorizations or
     * configurations has been parsed.
     *
     * @return A user mapping containing all authorizations and configurations
     *         parsed so far.
     */
    public UserMapping asUserMapping() {
        return user_mapping;
    }

}
