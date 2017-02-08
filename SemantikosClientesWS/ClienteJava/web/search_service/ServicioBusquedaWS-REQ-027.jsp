<%@ page import="cl.minsal.semantikos.ws.shared.CrossmapSetMembersResponse" %>
<%@ page import="cl.minsal.semantikos.ws.shared.Stringer" %>
<%@ page import="cl.minsal.semantikos.ws.shared.IndirectCrossmapsSearch" %>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<header>
    <title>Semantikos - WS-REQ-027: Obtener los CrossMapsetMembers directo por ID Descripción</title>

    <!-- Bootstrap -->
    <link href="<%=request.getContextPath()%>/css/bootstrap-3.3.7/css/bootstrap.css" rel="stylesheet"
          property='stylesheet'>
</header>

<!-- Se incluye el índice de TABs -->
<jsp:include page="ServicioBusquedaHeader.html"/>


<div class="container">
    <h4>Semantikos - WS-REQ-027: Obtener los CrossMapsetMembers directo por ID Descripción</h4>

    <form method="post" action="<%=request.getContextPath()%>/ws-req-027">

        <!-- El DESCRIPTION ID de la descripción a buscar -->
        <div class="form-group">
            <label for="description_ID">Description ID</label>
            <input type="text" id="description_ID" name="description_ID" class="form-control"
                   placeholder="Ingrese los DESCRIPTION ID separados por ','" required="required">
        </div>

        <!-- El CONCEPT ID de la descripción a buscar -->
        <div class="form-group">
            <label for="concept_ID">Concept ID</label>
            <input type="text" id="concept_ID" name="concept_ID" class="form-control"
                   placeholder="Ingrese los DESCRIPTION ID separados por ','" required="required">
        </div>

        <!-- El Identificador del Establecimiento-->
        <div class="form-group">
            <label for="stablishment_id">Identificador Establecimiento</label>
            <input id="stablishment_id" name="stablishment_id" type="text" class="form-control"
                   placeholder="Ingrese el identificador de su establecimiento"
                   required="required">
        </div>

        <button type="submit">Invocar Servicio</button>

        <c:if test="${requestScope.serviceResponse != null}">
            <div id="Response">
                <jsp:useBean id="stringer" class="cl.minsal.semantikos.ws.shared.Stringer"
                             type="cl.minsal.semantikos.ws.shared.Stringer">
                    CrossmapSet members: <%= Stringer.toString(((IndirectCrossmapsSearch) request.getAttribute("serviceResponse"))) %>
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