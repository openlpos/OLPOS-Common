<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%-- Layout Tiles 
  This layout create a html page with <header> and <body> tags. It render
   a header, body and footer tile.
  @param header Header tile (jsp url or definition name)
  @param body Body
  @param footer Footer
--%>
<html>
  <head>

    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/default.css" type="text/css" />
    
    <title>Mercator</title>
    
  </head>

<body>

<center>
    <!-- header -->
    <table border="0" cellpadding="0" cellspacing="0" width="100%">
       <tr>
            <td align="center">
                <tiles:insert attribute="header" />
            </td>
        </tr>
    </table>
                

     <table border="0" cellpading="0" cellspacing="0" width="100%" style="margin-top:2" class="main">
         <tr>
             <td width="0" valign="top" class="panelLeft">
               &#160;
             </td>
             <td width="100%" valign="top"  align="center"  height="400" class="panelMain">
                 <div style="margin-top:10;margin-bottom:10">
                     <tiles:insert attribute='body' />
                 </div>
             </td>
          </tr>
     </table>
     <table border="0" cellpading="0" cellspacing="0" width="100%" style="margin-top:0" class="footer">
          <tr>
             <td colspan="2" width="100%">
                   <tiles:insert attribute='footer' />
             </td>
          </tr>
     </table>

                

</center>
</body>
</html>

