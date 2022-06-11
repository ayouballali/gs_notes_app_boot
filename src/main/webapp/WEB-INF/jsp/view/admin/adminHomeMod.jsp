<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags"%>


<jsp:include page="../fragments/adminHeader.jsp" />

<div class="container">

    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <div class="container-fluid">

            <jsp:include page="../fragments/menu.jsp" />

        </div>
    </nav>








    <form ACTION="admin/processMod">
    <table class="table">
        <thead>
        <tr>
            <th scope="col">cne de base D</th>
            <th scope="col">nom  de base D</th>
            <th scope="col">prenom  de base D</th>
            <th scope="col">cne de exl</th>
            <th scope="col">nom de exl</th>
            <th scope="col">prenom de exel</th>
            <th>mise ajour ?</th>
        </tr>
        </thead>
        <c:forEach var="i" begin="0" end="${cont-1}">
    <tr>


        <td><c:out value="${listEtd[i].cne}" /></td>
        <td><c:out value="${listEtd[i].nom} " /></td>
        <td><c:out value="${listEtd[i].prenom} " /></td>
        <td><c:out value="${listEtdexl[i].cne}" /></td>
        <td><c:out value="${listEtdexl[i].nom} " /></td>
        <td><c:out value="${listEtdexl[i].prenom} " /></td>


        <td>
            <input type="checkbox" name="${listEtd[i].cne}" value="${listEtd[i].cne}"> check<BR>
        </td>

    </tr>

    </c:forEach>
    </table>
        <input type="submit" value="Submit">
    </form>
    ${message2}

    <jsp:include page="../fragments/adminfooter.jsp" />
