<%@ page import="cl.minsal.semantikos.ws.shared.Stringer" %>
<%@ page import="cl.minsal.semantikos.ws.shared.RespuestaConceptosPorCategoria" %>
<%@ page import="cl.minsal.semantikos.ws.shared.RespuestaBuscarTermino" %>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<header>
    <title>Semantikos - WS-REQ-005: Buscar términos Pedibles</title>

    <!-- Bootstrap -->
    <link href="<%=request.getContextPath()%>/css/bootstrap-3.3.7/css/bootstrap.css" rel="stylesheet"
          property='stylesheet'>
</header>

<!-- Se incluye el índice de TABs -->
<jsp:include page="ServicioBusquedaHeader.html"/>


<div class="container">
    <h4>Semantikos - WS-REQ-005: Buscar términos <em>Pedibles</em></h4>

    <form method="post" action="<%=request.getContextPath()%>/ws-req-005">

        <!-- El término a buscar -->
        <div class="form-group">
            <label for="requestable">Concepto Pedible?</label>
            <select id="requestable" name="requestable" required="required">
                <option value="Si" selected="selected">Si</option>
                <option value="No">No</option>
            </select>
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
                    Conceptos encontrados: <%= Stringer.toString(((RespuestaBuscarTermino) request.getAttribute("serviceResponse")).getConceptos()) %>
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