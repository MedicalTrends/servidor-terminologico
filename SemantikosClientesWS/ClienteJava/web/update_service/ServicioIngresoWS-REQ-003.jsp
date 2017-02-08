<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<header>
    <title>Semantikos - WS-REQ-031: Proponer un término para codificación</title>

    <!-- Bootstrap -->
    <link href="<%=request.getContextPath()%>/css/bootstrap-3.3.7/css/bootstrap.css" rel="stylesheet"
          property='stylesheet'>
</header>

<!-- Se incluye el índice de TABs -->
<jsp:include page="ServicioIngresoHeader.html"/>


<div class="container">
    <h4>WS-REQ-031: Proponer <em>término</em> a codificar</h4>

    <form method="post" action="<%=request.getContextPath()%>/ws-req-031">

        <div class="form-group">
            <label for="term">Término</label>
            <input id="term" name="term" type="text" class="form-control" placeholder="Ingrese el término propuesto">
        </div>

        <div class="form-group">
            <label for="category">Categoría propuesta</label>
            <input id="category" name="category" type="text" class="form-control"
                   placeholder="Ingrese la categoría propuesta">
        </div>

        <div class="form-group">
            <label for="profesional">Nombre Profesional</label>
            <input id="profesional" name="profesional" type="text" class="form-control"
                   placeholder="Ingrese el nombre del profesional">
        </div>

        <div class="form-group">
            <label for="email">Mail</label>
            <input id="email" name="email" type="email" class="form-control" placeholder="Ingrese mail del profesional">
        </div>

        <!-- Profesión -->
        <div class="form-group">
            <label for="profession">Profesión</label>
            <input id="profession" name="profession" type="text" class="form-control"
                   placeholder="Ingrese la profesión del profesional">
        </div>

        <div class="form-group">
            <label for="specialty">Especialidad</label>
            <input id="specialty" name="specialty" type="text" class="form-control"
                   placeholder="Ingrese la especialidad del profesional">
        </div>

        <div class="form-group">
            <label for="subSpecialty">Sub-Especialidad</label>
            <input id="subSpecialty" name="subSpecialty" type="text" class="form-control"
                   placeholder="Ingrese la sub-especialidad del profesional">
        </div>

        <div class="form-group">
            <label for="establishment">Establecimiento</label>
            <input id="establishment" name="establishment" type="text" class="form-control"
                   placeholder="Ingrese el codigo del establecimiento del profesional">
        </div>

        <div class="form-group">
            <label for="sensitiveCase">Sensible a mayúsculas?</label>
            <select id="sensitiveCase" name="sensitiveCase">
                <option value="true">Si</option>
                <option value="true" selected="selected">No</option>
            </select>
        </div>

        <div class="form-group">
            <label for="observation">Observación</label>
            <textarea id="observation" name="observation" class="form-control"
                      placeholder="Ingrese alguna observación para entender el contexto del término propuesto."></textarea>
        </div>

        <div class="form-group">
            <label for="descriptionType">Tipo de descripción propuesta</label>
            <textarea id="descriptionType" name="descriptionType" class="form-control"
                      placeholder="Ingrese alguna observación para entender el contexto del término propuesto."></textarea>
        </div>

        <button type="submit">Invocar Servicio</button>

        <div id="Response">
            DESCRIPTION ID de la descripción creada: ${requestScope.serviceResponse.idDescripcion} <br/>
        </div>

        <c:if test="${requestScope.exception != null}">
            <div id="Error">
                <p>Se ha producido un error:</p>
                    ${requestScope.exception}
            </div>
        </c:if>
    </form>
</div>

</html>