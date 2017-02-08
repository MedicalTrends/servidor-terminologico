<%@ page import="cl.minsal.semantikos.ws.shared.RespuestaRefSets" %>
<%@ page import="cl.minsal.semantikos.ws.shared.Stringer" %>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<header>
    <title>Semantikos - WS-REQ-008: Obtener REFSETs</title>

    <!-- Bootstrap -->
    <link href="<%=request.getContextPath()%>/css/bootstrap-3.3.7/css/bootstrap.css" rel="stylesheet"
          property='stylesheet'>
</header>

<!-- Se incluye el índice de TABs -->
<jsp:include page="ServicioBusquedaHeader.html"/>


<div class="container">
    <h4>Semantikos - WS-REQ-008: Obtener REFSETs</h4>

    <form method="post" action="<%=request.getContextPath()%>/ws-req-008">

        <!-- Refsets -->
        <div class="form-group">
            <label for="includeStablishments">Incluye establecimiento</label>
            <select id="includeStablishments" name="includeStablishments" class="form-control" required="required">
                <option selected="selected" value="true">Si</option>
                <option value="false">No</option>
            </select>
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