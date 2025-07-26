<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="container">
    <div class="row">
        <div class="col-md-12 text-center">
            <ul class="nav nav-tabs justify-content-center" role="tablist" style="display: inline-block;">
                <li role="presentation" class="active">
                    <a href="#projetos" aria-controls="projetos" role="tab" data-toggle="tab">Projetos</a>
                </li>

            </ul>
        </div>
    </div>

    <div class="tab-content">
        <div role="tabpanel" class="tab-pane active" id="projetos">
            <div class="row" style="margin-top: 20px;">
                <c:forEach var="projeto" items="${projetos}" varStatus="status">
                    <div class="col-md-4 col-sm-6" style="margin-bottom: 20px;">
                        <div class="panel panel-default project-card"
                             onclick="abrirModalProjeto('${projeto.id}', '${projeto.nome}', '${projeto.descricao}', '${projeto.dataFim}')">
                            <div class="panel-heading">
                                <h4 class="panel-title">${projeto.nome}</h4>
                            </div>
                            <div class="panel-body">
                                <p><strong>Gerente:</strong> ${projeto.gerente.nome}</p>
                                <p><strong>Data Início:</strong> ${projeto.dataInicio}</p>
                                <p><strong>Previsão Término:</strong> ${projeto.dataPrevisaoFim}</p>
                                <p><strong>Status:</strong>
                                    <span class="label label-info">${projeto.status.descricao}</span>
                                </p>
                                <p><strong>Risco:</strong>
                                    <c:choose>
                                        <c:when test="${projeto.risco.name() == 'BAIXO_RISCO'}">
                                            <span class="label label-success">${projeto.risco.descricao}</span>
                                        </c:when>
                                        <c:when test="${projeto.risco.name() == 'MEDIO_RISCO'}">
                                            <span class="label label-warning">${projeto.risco.descricao}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="label label-danger">${projeto.risco.descricao}</span>
                                        </c:otherwise>
                                    </c:choose>
                                </p>
                                <p><strong>Orçamento:</strong>
                                    <c:if test="${projeto.orcamento != null}">
                                        R$ <fmt:formatNumber value="${projeto.orcamento}" type="currency"
                                                             currencySymbol=""/>
                                    </c:if>
                                    <c:if test="${projeto.orcamento == null}">
                                        Não informado
                                    </c:if>
                                </p>
                            </div>
                            <div class="panel-footer">
                                <button onclick="event.stopPropagation(); abrirModalEditarProjeto(${projeto.id})"
                                        class="btn btn-sm btn-primary">Editar
                                </button>

                                <button onclick="event.stopPropagation(); excluirProjeto(${projeto.id}, '${projeto.nome}')"
                                        class="btn btn-sm btn-danger">Excluir
                                </button>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
            <div class="row">
                <div class="col-md-12 text-center">
                    <button class="btn btn-success" onclick="abrirModalNovoProjeto()">
                        <span class="glyphicon glyphicon-plus"></span> Novo Projeto
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
