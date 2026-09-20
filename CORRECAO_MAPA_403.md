# Correção do mapa — erro HTTP 403

## O que causava o erro
O site anterior carregava tiles de `*.tile.openstreetmap.org` e aplicava `Referrer-Policy: no-referrer`. A política oficial dos servidores comunitários do OpenStreetMap exige identificação e Referer válido no navegador, além de cache apropriado; seus servidores podem bloquear requisições (403).

## Correção adotada
- Removidos os tiles OSM e o carregamento do Leaflet por CDN. Nenhum token de API ou serviço de terceiros é necessário para visualizar o mapa regional de São Paulo.
- Implementado mapa regional SVG responsivo e autossuficiente com grade de latitude/longitude, referências de cidades e marcadores clicáveis **somente** para unidades credenciadas em SP com coordenadas geográficas verificadas.
- Atualizada a política CSP para remover a autorização de scripts, estilos e imagens dos serviços externos anteriores.
- O desenho é **referência geográfica esquemática**, não mapa rodoviário nem mapa oficial com ruas. A localização dos marcadores segue projeção linear das coordenadas cadastradas; não inferimos parceria nem localização não validada.
- Quando não houver parceiros credenciados com coordenadas, o mapa permanece visível e informa claramente a ausência de marcadores; a lista de unidades do sistema continua disponível.

## Limitações e publicação
As agendas disponíveis dependem de credenciamento e publicação pela própria instituição; este ajuste não cria hospitais parceiros, horários ou integrações hospitalares reais. Para usar mapas de ruas em produção, contrate/configure um provedor autorizado com SLA ou hospede mapas próprios, valide requisitos de licença, proteção de dados, acessibilidade e manutenção.

Referência: https://operations.osmfoundation.org/policies/tiles/
