<%@ page import="cl.minsal.semantikos.ws.shared.RespuestaBuscarTerminoGenerica" %>
<%@ page import="cl.minsal.semantikos.ws.shared.Stringer" %>
<%@ page import="cl.minsal.semantikos.ws.shared.RespuestaConceptosPorCategoria" %>
<%@ page import="cl.minsal.semantikos.ws.shared.HTMLer" %>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<header>
    <title>Semantikos - WS-REQ-002: Obtener conceptos de una categoría</title>

    <!-- Bootstrap -->
    <link href="<%=request.getContextPath()%>/css/bootstrap-3.3.7/css/bootstrap.css" rel="stylesheet"
          property='stylesheet'>
</header>

<!-- Se incluye el índice de TABs -->
<jsp:include page="ServicioBusquedaHeader.html"/>


<div class="container">
    <h4>WS-REQ-002: Obtener <em>conceptos</em> por categoría</h4>

    <form method="post" action="<%=request.getContextPath()%>/ws-req-002">

        <!-- Param 1: Categoría asociada a los conceptos -->
        <div class="form-group">
            <label for="category">Categoría</label>
            <input id="category" name="category" type="text" class="form-control"
                   placeholder="Ingrese las categorías separadas por ','">
        </div>

        <!-- Param 2: ID Establecimiento -->
        <div class="form-group">
            <label for="idStablishment">ID Establecimiento</label>
            <input id="idStablishment" name="idStablishment" type="text" class="form-control"
                   placeholder="Ingrese el identificador del establecimiento">
        </div>

        <button type="submit">Invocar Servicio</button>

        <c:if test="${requestScope.serviceResponse != null}">
            <div id="Response">
                Conceptos:
                <%= HTMLer.toHTML(((RespuestaConceptosPorCategoria) request.getAttribute("serviceResponse")).getConceptos()) %>
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