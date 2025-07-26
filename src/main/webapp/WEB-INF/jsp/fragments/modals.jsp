<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="modal fade" id="confirmDeleteModal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Confirmar Exclusão</h4>
            </div>
            <div class="modal-body">
                <p>Tem certeza de que deseja excluir o projeto <strong id="projeto-nome"></strong>?</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                <button type="button" class="btn btn-danger" id="confirmar-exclusao">Excluir</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="projectDetailsModal" tabindex="-1" role="dialog">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title" id="modal-projeto-nome">Detalhes do Projeto</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-6">
                        <h5><strong>Descrição</strong></h5>
                        <p id="modal-projeto-descricao">Carregando...</p>
                    </div>
                    <div class="col-md-6">
                        <h5><strong>Data Real de Término</strong></h5>
                        <p id="modal-projeto-data-fim">Carregando...</p>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <h5><strong>Membros da Equipe</strong></h5>
                        <div id="modal-projeto-membros">
                            <p>Carregando membros...</p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Fechar</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="newProjectModal" tabindex="-1" role="dialog">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Novo Projeto</h4>
            </div>
            <div class="modal-body">
                <form id="newProjectForm">
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="new-nome">Nome do Projeto*</label>
                                <input type="text" class="form-control" id="new-nome" required>
                            </div>

                            <div class="form-group">
                                <label for="new-data-inicio">Data de Início*</label>
                                <input type="date" class="form-control" id="new-data-inicio" required>
                            </div>

                            <div class="form-group">
                                <label for="new-data-previsao-fim">Data de Previsão de Término*</label>
                                <input type="date" class="form-control" id="new-data-previsao-fim" required>
                            </div>

                            <div class="form-group">
                                <label for="new-data-fim">Data Real de Término</label>
                                <input type="date" class="form-control" id="new-data-fim">
                            </div>
                        </div>

                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="new-gerente">Gerente Responsável*</label>
                                <select class="form-control" id="new-gerente" required>
                                    <option value="">Selecione um gerente</option>
                                </select>
                            </div>

                            <div class="form-group">
                                <label for="new-status">Status*</label>
                                <select class="form-control" id="new-status" required>
                                    <option value="EM_ANALISE">Em Análise</option>
                                    <option value="ANALISE_REALIZADA">Análise Realizada</option>
                                    <option value="ANALISE_APROVADA">Análise Aprovada</option>
                                    <option value="INICIADO">Iniciado</option>
                                    <option value="PLANEJADO">Planejado</option>
                                    <option value="EM_ANDAMENTO">Em Andamento</option>
                                    <option value="ENCERRADO">Encerrado</option>
                                    <option value="CANCELADO">Cancelado</option>
                                </select>
                            </div>

                            <div class="form-group">
                                <label for="new-orcamento">Orçamento (R$)</label>
                                <input type="number" step="0.01" class="form-control" id="new-orcamento">
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="new-descricao">Descrição</label>
                        <textarea class="form-control" id="new-descricao" rows="4"></textarea>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                <button type="button" class="btn btn-success" id="btn-criar-projeto">Criar Projeto</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="editProjectModal" tabindex="-1" role="dialog">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Editar Projeto</h4>
            </div>
            <div class="modal-body">
                <form id="editProjectForm">
                    <input type="hidden" id="edit-projeto-id">

                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="edit-nome">Nome do Projeto*</label>
                                <input type="text" class="form-control" id="edit-nome" required>
                            </div>

                            <div class="form-group">
                                <label for="edit-data-inicio">Data de Início*</label>
                                <input type="date" class="form-control" id="edit-data-inicio" required>
                            </div>

                            <div class="form-group">
                                <label for="edit-data-previsao-fim">Data de Previsão de Término*</label>
                                <input type="date" class="form-control" id="edit-data-previsao-fim" required>
                            </div>

                            <div class="form-group">
                                <label for="edit-data-fim">Data Real de Término</label>
                                <input type="date" class="form-control" id="edit-data-fim">
                            </div>
                        </div>

                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="edit-gerente">Gerente Responsável*</label>
                                <select class="form-control" id="edit-gerente" required>
                                    <option value="">Selecione um gerente</option>
                                </select>
                            </div>

                            <div class="form-group">
                                <label for="edit-status">Status*</label>
                                <select class="form-control" id="edit-status" required>
                                    <option value="EM_ANALISE">Em Análise</option>
                                    <option value="ANALISE_REALIZADA">Análise Realizada</option>
                                    <option value="ANALISE_APROVADA">Análise Aprovada</option>
                                    <option value="INICIADO">Iniciado</option>
                                    <option value="PLANEJADO">Planejado</option>
                                    <option value="EM_ANDAMENTO">Em Andamento</option>
                                    <option value="ENCERRADO">Encerrado</option>
                                    <option value="CANCELADO">Cancelado</option>
                                </select>
                            </div>

                            <div class="form-group">
                                <label for="edit-orcamento">Orçamento (R$)</label>
                                <input type="number" step="0.01" class="form-control" id="edit-orcamento">
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-descricao">Descrição</label>
                        <textarea class="form-control" id="edit-descricao" rows="4"></textarea>
                    </div>
                </form>

                <hr>
                <h4>Membros do Projeto</h4>

                <div id="edit-membros-container">
                    <p class="text-muted">Carregando membros...</p>
                </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                <button type="button" class="btn btn-primary" id="btn-salvar-projeto">Salvar Alterações</button>
            </div>
        </div>
    </div>
</div>
