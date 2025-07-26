let funcionarios = [];

function carregarMembrosProjeto(projetoId) {
    const membrosContainer = document.getElementById('modal-projeto-membros');
    membrosContainer.innerHTML = '<p>Carregando membros...</p>';

    fetch('/projetos/' + projetoId + '/membros', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(function (response) {
            return response.json();
        })
        .then(function (membros) {
            if (membros && membros.length > 0) {
                var membrosHtml = '<ul class="list-group">';

                for (var i = 0; i < membros.length; i++) {
                    var membroAtual = membros[i];
                    var nomePessoa = membroAtual.pessoa ? membroAtual.pessoa.nome : 'Nome não disponível';
                    var atribuicao = membroAtual.atribuicao ? membroAtual.atribuicao : 'Não informada';

                    membrosHtml += '<li class="list-group-item">' +
                        '<strong>' + nomePessoa + '</strong>' +
                        '<br>' +
                        '<small class="text-muted">Atribuição: ' + atribuicao + '</small>' +
                        '</li>';
                }

                membrosHtml += '</ul>';
                membrosContainer.innerHTML = membrosHtml;
            } else {
                membrosContainer.innerHTML = '<p class="text-muted">Nenhum membro encontrado para este projeto.</p>';
            }
        })
        .catch(function (error) {
            console.error('Erro ao carregar membros:', error);
            membrosContainer.innerHTML = '<p class="text-danger">Erro ao carregar membros do projeto.</p>';
        });
}

function carregarMembrosProjetoParaEdicao(projetoId) {
    const membrosContainer = document.getElementById('edit-membros-container');
    membrosContainer.innerHTML = '<p class="text-muted">Carregando membros...</p>';

    fetch('/projetos/' + projetoId + '/membros', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro ao carregar membros');
            }
            return response.json();
        })
        .then(membros => {
            console.log("membros", membros)
            if (membros && membros.length > 0) {
                let membrosHtml = '<div class="list-group">';

                membros.forEach(membro => {
                    const nomePessoa = membro.pessoa ? membro.pessoa.nome : 'Nome não disponível';
                    const atribuicao = membro.atribuicao ? membro.atribuicao : 'Não informada';
                    const pessoaId = membro.pessoa ? membro.pessoa.id : null;

                    membrosHtml += `
                <div class="list-group-item">
                    <div class="row">
                        <div class="col-md-5">
                            <strong>${nomePessoa}</strong>
                        </div>
                        <div class="col-md-5">
                            <span>Atribuição: ${atribuicao}</span>
                        </div>
                        <div class="col-md-2 text-right">
                            <button type="button" class="btn btn-xs btn-danger" 
                                onclick="removerMembroProjeto(${projetoId}, ${pessoaId})">
                                <span class="glyphicon glyphicon-trash"></span> Remover
                            </button>
                        </div>
                    </div>
                </div>
            `;
                });

                membrosHtml += '</div>';
                membrosContainer.innerHTML = membrosHtml;
            } else {
                membrosContainer.innerHTML = '<p class="text-muted">Nenhum membro encontrado para este projeto.</p>';
            }
        })
        .catch(error => {
            console.error('Erro:', error);
            membrosContainer.innerHTML = '<p class="text-danger">Erro ao carregar membros do projeto.</p>';
        });
}

function removerMembroProjeto(projetoId, pessoaId) {
    if (!confirm('Tem certeza que deseja remover este membro do projeto?')) {
        return;
    }

    fetch(`/projetos/${projetoId}/membros/${pessoaId}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                if (response.headers.get('content-type')?.includes('text/plain')) {
                    return response.text().then(text => {
                        throw new Error(text)
                    });
                }
                throw new Error('Erro ao remover membro do projeto');
            }

                        carregarMembrosProjetoParaEdicao(projetoId);
            showToast('Membro removido com sucesso!', 'success');
        })
        .catch(error => {
            console.error('Erro:', error);
            showToast('Erro ao remover membro: ' + error.message, 'error');
        });
}



document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('btn-adicionar-membro').addEventListener('click', function () {
        const projetoId = document.getElementById('edit-projeto-id').value;
        const pessoaId = document.getElementById('novo-membro-pessoa').value;
        const atribuicao = document.getElementById('novo-membro-atribuicao').value;

        if (!pessoaId) {
            showToast('Por favor, selecione um funcionário', 'error');
            return;
        }

        const formData = new URLSearchParams();
        formData.append('pessoaId', pessoaId);
        formData.append('atribuicao', atribuicao);

        fetch(`/projetos/${projetoId}/membros`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: formData
        })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(text => {
                        throw new Error(text)
                    });
                }
                return response.json();
            })
            .then(data => {
                                document.getElementById('novo-membro-pessoa').value = '';
                document.getElementById('novo-membro-atribuicao').value = 'Funcionário';

                                carregarMembrosProjetoParaEdicao(projetoId);
                showToast('Membro adicionado com sucesso!', 'success');
            })
            .catch(error => {
                console.error('Erro:', error);
                showToast('Erro ao adicionar membro: ' + error.message, 'error');
            });
    });
});
