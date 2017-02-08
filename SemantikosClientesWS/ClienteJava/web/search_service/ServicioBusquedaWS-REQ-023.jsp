<%@ page import="cl.minsal.semantikos.ws.shared.RespuestaRefSets" %>
<%@ page import="cl.minsal.semantikos.ws.shared.Stringer" %>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<header>
    <title>Semantikos - WS-REQ-023: Obtener Todos las descripciones de un REFSET </title>

    <!-- Bootstrap -->
    <link href="<%=request.getContextPath()%>/css/bootstrap-3.3.7/css/bootstrap.css" rel="stylesheet"
          property='stylesheet'>
</header>

<!-- Se incluye el índice de TABs -->
<jsp:include page="ServicioBusquedaHeader.html"/>


<div class="container">
    <h4>Semantikos - WS-REQ-023: Obtener Todos las descripciones de un REFSET </h4>

    <form method="post" action="<%=request.getContextPath()%>/ws-req-023">

        <!-- RefSets-->
        <div class="form-group">
            <label for="refSetName">Nombre RefSet</label>
            <input type="text" id="refSetName" name="refSetName" class="form-control"
                   placeholder="Ingrese los RefSets separados por ','" required="required">
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
                    Conceptos encontrados: <%= Stringer.toString(((RespuestaRefSets) request.getAttribute("serviceResponse")).getRefsets()) %>
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