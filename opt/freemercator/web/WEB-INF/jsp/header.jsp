<%@ page import="com.globalretailtech.util.*, java.sql.*, com.globalretailtech.data.*" contentType="text/html"%>
<%
    Site site = new Site();

    DBContext dbContext = Application.dbConnection();
    String sql = Site.getByID(1);
    ResultSet rst = dbContext.executeWithResult (sql);
    if ( rst.next() )
        site.populate (rst);
%>
<!-- Header -->
<table width="100%">
    <tr>
        <td align="left" width="130"><a href="http://freemercator.sourceforge.net/"><img src="/images/logo.gif" border="0" alt="Free Mercator !"></td> 
        <td align="left" valign="top" width="60%">
            <table width="100%">
                <tr><td align="center" valign="top">
                <table width="75%" border="0">
                    <tr>
                        <td class="site"><%=site.name()%></td>
                    </tr><tr>   
                        <td style="font-size:8pt">#<%=site.siteNo()%>, merc. ID:<%=site.merchantID()%></td>
                    </tr><tr>   
                        <td style="font-size:8pt" class="address"><%=site.addr1()%></td>
                    </tr><tr>   
                        <td style="font-size:8pt" class="address"><%=site.addr2()%></td>
                    </tr><tr>   
                        <td style="font-size:8pt">Phone: <%=site.phone()%></td>
                    </tr>
                </table>
                
                </td></tr>
            </table>
        </td>
        <td align="right"  width="20%" valign="top" style="font-size:8pt;color:gray">
            <%=(new java.util.Date())%></td>
    </tr>
</table>
<!-- Header -->
