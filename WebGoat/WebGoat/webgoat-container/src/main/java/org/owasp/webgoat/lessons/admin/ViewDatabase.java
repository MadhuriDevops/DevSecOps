package org.owasp.webgoat.lessons.admin;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.owasp.webgoat.lessons.*;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.Input;
import org.owasp.webgoat.session.*;
/**
 * ************************************************************************************************
 * <p>
 * <p>
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 * <p>
 * Copyright (c) 2002 - 20014 Bruce Mayhew
 * <p>
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 * <p>
 * Getting Source ==============
 * <p>
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software
 * projects.
 *
 * @author Jeff Williams <a href="http://www.aspectsecurity.com">Aspect Security</a>
 * @version $Id: $Id
 * @since October 28, 2003
 */
public class ViewDatabase extends LessonAdapter {
    private final static String SALARY = "salary";
    /**
     * {@inheritDoc}
     * <p>
     * Description of the Method
     */
     protected Element createContent(WebSession s) {
         ElementContainer ec = new ElementContainer();
         try {
             ec.addElement(new StringElement("Enter a new salary for rlupin: "));
             StringBuffer salaryValue = new StringBuffer(s.getParser().getRawParameter(SALARY, ""));
             Input input = new Input(Input.TEXT, SALARY, salaryValue.toString());
             ec.addElement(input);
             Element b = ECSFactory.makeButton("Submit!");
             ec.addElement(b);
             Connection connection = DatabaseUtilities.getConnection(s);
             if (salaryValue.length() > 0) {
                 PreparedStatement statement = connection.prepareStatement("INSERT INTO salaries VALUES (?,?);");
                 statement.setString(1, "rlupin");
                 statement.setString(2, salaryValue.toString());
                 statement.execute();
                 Statement selectStatement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                         ResultSet.CONCUR_READ_ONLY);
                 ResultSet results = selectStatement.executeQuery("SELECT * FROM salaries");
                 if ((results != null) && (results.first())) {
                     makeSuccess(s);
                     ResultSetMetaData resultsMetaData = results.getMetaData();
                     ec.addElement(DatabaseUtilities.writeTable(results, resultsMetaData));
                 }
             }
         } catch (Exception e) {
             s.setMessage("Error generating " + this.getClass().getName());
             e.printStackTrace();
         }
         return (ec);
     }
    /**
     * Gets the category attribute of the DatabaseScreen object
     *
     * @return The category value
     */
    protected Category getDefaultCategory() {
        return Category.ADMIN_FUNCTIONS;
    }
    private final static Integer DEFAULT_RANKING = new Integer(1000);
    /**
     * <p>getDefaultRanking.</p>
     *
     * @return a {@link java.lang.Integer} object.
     */
    protected Integer getDefaultRanking() {
        return DEFAULT_RANKING;
    }
    /**
     * {@inheritDoc}
     * <p>
     * Gets the hints attribute of the DatabaseScreen object
     */
    protected List<String> getHints(WebSession s) {
        List<String> hints = new ArrayList<String>();
        hints.add("SELECT * FROM salaries");
        hints.add("DELETE FROM salaries");
        hints.add("SELECT * FROM salaries");
        return hints;
    }
    /**
     * {@inheritDoc}
     * <p>
     * Gets the instructions attribute of the ViewDatabase object
     */
    public String getInstructions(WebSession s) {
        String instructions = "This lesson is designed to demonstrate, how it might be easy to make SQL injection via simple input field.";
        return (instructions);
    }
    /**
     * Gets the role attribute of the ViewDatabase object
     *
     * @return The role value
     */
    public String getRole() {
        return HACKED_ADMIN_ROLE;
    }
    /**
     * Gets the title attribute of the DatabaseScreen object
     *
     * @return The title value
     */
    public String getTitle() {
        return ("CMT SQL injection lesson");
    }
}