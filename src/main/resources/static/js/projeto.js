let projetoIdParaExcluir = null;

function excluirProjeto(id, nome) {
    projetoIdParaExcluir = id;
    document.getElementById('projeto-nome').textContent = nome;
    $('#confirmDeleteModal').modal('show');
}

function abrirModalProjeto(projetoId, nome, descricao, dataRealFim) {
    document.getElementById('modal-projeto-nome').textContent = nome;
    document.getElementById('modal-projeto-descricao').textContent = descricao || 'Não informada';
    document.getElementById('modal-projeto-data-fim').textContent = dataRealFim || 'Não informada';

    carregarMembrosProjeto(projetoId);

    $('#projectDetailsModal').modal('show');
}

function abrirModalNovoProjeto() {
    document.getElementById('newProjectForm').reset();

    document.getElementById('new-status').value = 'EM_ANALISE';

    document.getElementById('new-data-inicio').value = new Date().toISOString().split('T')[0];

    carregarGerentesParaNovoProjeto();

    $('#newProjectModal').modal('show');
}

function carregarGerentesParaNovoProjeto() {
    fetch('/api/pessoas/gerentes', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro ao carregar gerentes');
            }
            return response.json();
        })
        .then(data => {
            const select = document.getElementById('new-gerente');

            select.innerHTML = '<option value="">Selecione um gerente</option>';

            data.forEach(gerente => {
                const option = document.createElement('option');
                option.value = gerente.id;
                option.textContent = gerente.nome;
                select.appendChild(option);
            });
        })
        .catch(error => {
            console.error('Erro:', error);
            showToast('Erro ao carregar lista de gerentes: ' + error.message, 'error');
        });
}

function abrirModalEditarProjeto(projetoId) {
    document.getElementById('editProjectForm').reset();
    document.getElementById('edit-projeto-id').value = projetoId;

    carregarDadosProjeto(projetoId);

    carregarGerentes();

    carregarMembrosProjetoParaEdicao(projetoId);


    $('#editProjectModal').modal('show');
}

function carregarDadosProjeto(projetoId) {
    fetch('/projetos/' + projetoId, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro ao carregar dados do projeto');
            }
            return response.json();
        })
        .then(projeto => {
                        document.getElementById('edit-nome').value = projeto.nome || '';

                        if (projeto.dataInicio) {
                document.getElementById('edit-data-inicio').value = projeto.dataInicio;
            }
            if (projeto.dataPrevisaoFim) {
                document.getElementById('edit-data-previsao-fim').value = projeto.dataPrevisaoFim;
            }
            if (projeto.dataFim) {
                document.getElementById('edit-data-fim').value = projeto.dataFim;
            }

            document.getElementById('edit-descricao').value = projeto.descricao || '';
            document.getElementById('edit-status').value = projeto.status || 'EM_ANALISE';
            document.getElementById('edit-orcamento').value = projeto.orcamento || '';

                        if (projeto.gerente && projeto.gerente.id) {
                setTimeout(() => {
                    document.getElementById('edit-gerente').value = projeto.gerente.id;
                }, 300);             }
        })
        .catch(error => {
            console.error('Erro:', error);
            showToast('Erro ao carregar dados do projeto: ' + error.message, 'error');
        });
}

function carregarGerentes() {
    fetch('/api/pessoas/gerentes', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro ao carregar gerentes');
            }
            return response.json();
        })
        .then(data => {
            gerentes = data;
            const select = document.getElementById('edit-gerente');

                        select.innerHTML = '<option value="">Selecione um gerente</option>';

                        gerentes.forEach(gerente => {
                const option = document.createElement('option');
                option.value = gerente.id;
                option.textContent = gerente.nome;
                select.appendChild(option);
            });
        })
        .catch(error => {
            console.error('Erro:', error);
            showToast('Erro ao carregar lista de gerentes: ' + error.message, 'error');
        });
}

document.addEventListener('DOMContentLoaded', function () {
        document.getElementById('btn-criar-projeto').addEventListener('click', function () {
        const form = document.getElementById('newProjectForm');

                if (!form.checkValidity()) {
                        const submitButton = document.createElement('button');
            form.appendChild(submitButton);
            submitButton.click();
            form.removeChild(submitButton);
            return;
        }

                const projetoRequest = {
            nome: document.getElementById('new-nome').value,
            dataInicio: document.getElementById('new-data-inicio').value,
            dataPrevisaoFim: document.getElementById('new-data-previsao-fim').value,
            dataFim: document.getElementById('new-data-fim').value || null,
            descricao: document.getElementById('new-descricao').value,
            status: document.getElementById('new-status').value,
            orcamento: document.getElementById('new-orcamento').value || null,
            gerenteId: document.getElementById('new-gerente').value
        };

                fetch('/projetos', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(projetoRequest)
        })
            .then(response => {
                if (!response.ok) {
                    if (response.headers.get('content-type')?.includes('text/plain')) {
                        return response.text().then(text => {
                            throw new Error(text)
                        });
                    }
                    throw new Error('Erro ao criar projeto');
                }
                return response.json();
            })
            .then(data => {
                showToast('Projeto criado com sucesso!', 'success');
                $('#newProjectModal').modal('hide');

                setTimeout(() => {
                    location.reload();
                }, 100);
            })
            .catch(error => {
                console.error('Erro:', error);
                showToast('Erro ao criar projeto: ' + error.message, 'error');
            });
    });

    document.getElementById('confirmar-exclusao').addEventListener('click', function () {
        if (projetoIdParaExcluir) {
                        fetch('/projetos/' + projetoIdParaExcluir + '/pode-ser-excluido', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                }
            })
                .then(response => response.json())
                .then(podeSerExcluido => {
                    if (!podeSerExcluido) {
                        showToast('Projeto não pode ser excluído.', 'error');
                        $('#confirmDeleteModal').modal('hide');
                        return;
                    }

                                        fetch('/projetos/' + projetoIdParaExcluir, {
                        method: 'DELETE',
                        headers: {
                            'Content-Type': 'application/json'
                        }
                    })
                        .then(response => {
                            if (response.ok) {
                                showToast('Projeto excluído com sucesso!', 'success');
                                setTimeout(() => {
                                    location.reload();                                 }, 1500);
                            } else {
                                return response.text();
                            }
                        })
                        .then(errorMessage => {
                            if (errorMessage) {
                                showToast('Erro ao excluir projeto: ' + errorMessage, 'error');
                            }
                        })
                        .catch(error => {
                            console.error('Erro:', error);
                            showToast('Erro inesperado ao excluir projeto. Tente novamente.', 'error');
                        });

                    $('#confirmDeleteModal').modal('hide');
                })
                .catch(error => {
                    console.error('Erro ao verificar se projeto pode ser excluído:', error);
                    showToast('Erro ao verificar se projeto pode ser excluído. Tente novamente.', 'error');
                    $('#confirmDeleteModal').modal('hide');
                });
        }
    });

        document.getElementById('btn-salvar-projeto').addEventListener('click', function () {
        const form = document.getElementById('editProjectForm');

                if (!form.checkValidity()) {
                        const submitButton = document.createElement('button');
            form.appendChild(submitButton);
            submitButton.click();
            form.removeChild(submitButton);
            return;
        }

        const projetoId = document.getElementById('edit-projeto-id').value;

                const projetoRequest = {
            nome: document.getElementById('edit-nome').value,
            dataInicio: document.getElementById('edit-data-inicio').value,
            dataPrevisaoFim: document.getElementById('edit-data-previsao-fim').value,
            dataFim: document.getElementById('edit-data-fim').value || null,
            descricao: document.getElementById('edit-descricao').value,
            status: document.getElementById('edit-status').value,
            orcamento: document.getElementById('edit-orcamento').value || null,
            gerenteId: document.getElementById('edit-gerente').value
        };

                fetch(`/projetos/${projetoId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(projetoRequest)
        })
            .then(response => {
                if (!response.ok) {
                    if (response.headers.get('content-type')?.includes('text/plain')) {
                        return response.text().then(text => {
                            throw new Error(text)
                        });
                    }
                    throw new Error('Erro ao atualizar projeto');
                }
                return response.json();
            })
            .then(() => {
                showToast('Projeto atualizado com sucesso!', 'success');
                $('#editProjectModal').modal('hide');

                setTimeout(() => {
                    location.reload();
                }, 100);
            })
            .catch(error => {
                console.error('Erro:', error);
                showToast('Erro ao atualizar projeto: ' + error.message, 'error');
            });
    });
});
