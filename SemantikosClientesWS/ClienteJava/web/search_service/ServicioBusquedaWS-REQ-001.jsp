<%@ page import="cl.minsal.semantikos.ws.shared.RespuestaBuscarTerminoGenerica" %>
<%@ page import="cl.minsal.semantikos.ws.shared.Stringer" %>
<%@ page import="cl.minsal.semantikos.ws.shared.HTMLer" %>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<header>
    <title>Semantikos - WS-REQ-01: Proponer un término para codificación</title>

    <!-- Bootstrap -->
    <link href="<%=request.getContextPath()%>/css/bootstrap-3.3.7/css/bootstrap.css" rel="stylesheet"
          property='stylesheet'>
</header>

<!-- Se incluye el índice de TABs -->
<jsp:include page="ServicioBusquedaHeader.html"/>


<div class="container">
    <h4>WS-REQ-001: Buscar <em>término</em> a codificar</h4>

    <form method="post" action="<%=request.getContextPath()%>/ws-req-001">

        <!-- El término a buscar -->
        <div class="form-group">
            <label for="term">Término</label>
            <input id="term" name="term" type="text" class="form-control" placeholder="Ingrese el término a buscar"
                   required="required">
        </div>

        <!-- Categorías -->
        <div class="form-group">
            <label for="categories">Categorías</label>
            <input id="categories" name="categories" type="text" class="form-control"
                   placeholder="Ingrese las categorías separadas por ','">
        </div>

        <!-- Refsets -->
        <div class="form-group">
            <label for="refsets">RefSets</label>
            <input id="refsets" name="refsets" type="text" class="form-control"
                   placeholder="Ingrese los RefSets ','">
        </div>

        <!-- El Identificador del Establecimiento-->
        <div class="form-group">
            <label for="idStablishment">Identificador Establecimiento</label>
            <input id="idStablishment" name="idStablishment" type="text" class="form-control" placeholder="Ingrese el identificador de su establecimiento"
                   required="required">
        </div>

        <button type="submit">Invocar Servicio</button>

        <c:if test="${requestScope.serviceResponse != null}">
            <div id="Response">
                <jsp:useBean id="stringer" class="cl.minsal.semantikos.ws.shared.Stringer"
                             type="cl.minsal.semantikos.ws.shared.Stringer">
                    Perfect: <%= HTMLer.toHTML(((RespuestaBuscarTerminoGenerica) request.getAttribute("serviceResponse")).getDescripcionesPerfectMatch()) %>
                    <br/>
                    No validas: ${requestScope.serviceResponse.descripcionesNoValidas} <br/>
                    Pendientes: ${requestScope.serviceResponse.descripcionesPendientes} <br/>
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