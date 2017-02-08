<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>

<header>
    <title>Semantikos - WS-REQ-003: Incrementar uso de un término</title>

    <!-- Bootstrap -->
    <link href="<%=request.getContextPath()%>/css/bootstrap-3.3.7/css/bootstrap.css" rel="stylesheet"
          property='stylesheet'>
</header>

<!-- Se incluye el índice de TABs -->
<jsp:include page="ServicioIngresoHeader.html"/>


<div class="container">
    <h4>WS-REQ-030: Incrementar <em>uso</em> de un término</h4>

    <form method="post" action="<%=request.getContextPath()%>/ws-req-030">
        <div class="form-group">
            <label for="DESCRIPTION_ID">DESCRIPTION ID</label>
            <input id="DESCRIPTION_ID" name="DESCRIPTION_ID" type="text" class="form-control"
                   placeholder="Ingrese el DESCRIPTION_ID de la descripción">
        </div>

        <button type="submit">Invocar Servicio</button>

        <div id="Response">
            Tipo: ${requestScope.serviceResponse.tipo} <br />
            Término: ${requestScope.serviceResponse.termino} <br />
            Usos: ${requestScope.serviceResponse.usos} <br />
        </div>
    </form>
</div>

</html>