<%@page contentType="text/html" pageEncoding="UTF-8"%>


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <p><h2>DashBoard</h2></p>
        <table border = "1">
            <tr>
                <td>Timestamp</td>
                <td>Collection Name</td>
                <td>Track Name</td>
                <td>Collection Price</td>
                <td>Track Price</td>
                <td>Country</td>
            </tr>
            <%String jsonString = (String) request.getAttribute("jsonString");
                String[] records = jsonString.split(";");
                for (int i = 0; i < records.length; i++) {
                    String[] record = records[i].split("&");
            %>
            <tr>
                <td><%=record[0]%></td>
                <td><%=record[1]%></td>
                <td><%=record[2]%></td>
                <td><%=record[3]%></td>
                <td><%=record[4]%></td>
                <td><%=record[5]%></td>
            <tr>
                <%
                    }
                %>
            <tr>
                <td>Highest Collection Price<td>
                <td>Highest Track Price<td>
                <td>Most Frequently Appeared Countries<td>
            </tr>
            <tr>
                
                <td ><%=request.getAttribute("maxCollectionPrice")%><td>
                <td ><%=request.getAttribute("maxTrackPrice")%><td>
                <td ><%=request.getAttribute("mostCountry")%><td>
            </tr>
        </table>
    </body>
</html>

