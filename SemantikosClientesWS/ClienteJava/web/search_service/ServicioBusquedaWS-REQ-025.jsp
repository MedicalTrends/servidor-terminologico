<%@ page import="cl.minsal.semantikos.ws.shared.CrossmapSetsResponse" %>
<%@ page import="cl.minsal.semantikos.ws.shared.Stringer" %>
<%@ page import="cl.minsal.semantikos.ws.shared.CrossmapSetMembersResponse" %>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<header>
    <title>Semantikos - WS-REQ-025: Obtener los CrossMapsetsMembers de CrossMapsets</title>

    <!-- Bootstrap -->
    <link href="<%=request.getContextPath()%>/css/bootstrap-3.3.7/css/bootstrap.css" rel="stylesheet"
          property='stylesheet'>
</header>

<!-- Se incluye el índice de TABs -->
<jsp:include page="ServicioBusquedaHeader.html"/>


<div class="container">
    <h4>Semantikos - WS-REQ-025: Obtener los CrossMapsetsMembers de CrossMapsets</h4>

    <form method="post" action="<%=request.getContextPath()%>/ws-req-025">

        <!-- Nombre Abreviado del Crossmap Set -->
        <div class="form-group">
            <label for="crossmapSetAbbreviatedName">Nombre Abreviado del Crossmap Set</label>
            <input id="crossmapSetAbbreviatedName" name="crossmapSetAbbreviatedName" type="text" class="form-control"
                   placeholder="Nombre Abreviado del Crossmap Set" required="required">
        </div>

        <!-- El Identificador del Establecimiento-->
        <div class="form-group">
            <label for="idStablishment">Identificador Establecimiento</label>
            <input id="idStablishment" name="idStablishment" type="text" class="form-control"
                   placeholder="Ingrese el identificador de su establecimiento"
                   required="required">
        </div>

        <button type="submit">Invocar Servicio</button>

        <c:if test="${requestScope.serviceResponse != null}">
            <div id="Response">
                <jsp:useBean id="stringer" class="cl.minsal.semantikos.ws.shared.Stringer"
                             type="cl.minsal.semantikos.ws.shared.Stringer">
                    CrossmapSet members: <%= Stringer.toString(((CrossmapSetMembersResponse) request.getAttribute("serviceResponse"))) %>
                    <br/>
                </jsp:useBean>
            </div>
        </c:if>

        <c:if test="${requestScope.exception != null}">
            <div id="Error">
                <p>Se ha producido un error:</p>
                    ${requestScope.exception}
            </div>
        </c:if>
    </form>
</div>

</html>