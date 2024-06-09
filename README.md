# MediaFireDownloader
[![en](https://img.shields.io/badge/ENGLISH-Click%20to%20Translate-red.svg)](README.en.md)

## Descrição
O `MediaFireDownloader` é uma biblioteca em Java desenvolvida para facilitar o download de arquivos do MediaFire em aplicativos Android. Através de uma interface simples e métodos intuitivos, a biblioteca permite iniciar, monitorar e gerenciar downloads diretamente do MediaFire, lidando com diversos eventos como início, progresso, conclusão e falha do download. 

### Características principais:
- **Download simplificado**: Inicie downloads de arquivos do MediaFire com facilidade, fornecendo apenas a URL e o caminho de destino.
- **Validação de URLs**: Verifique se uma URL é válida para download do MediaFire antes de iniciar o processo.
- **Eventos de download**: Receba notificações detalhadas sobre o estado do download através de uma interface de listener.
- **Manejo de erros**: Identifique e trate erros comuns durante o download, como links inválidos ou falhas no processo.

Esta biblioteca é ideal para desenvolvedores que desejam integrar a funcionalidade de download de arquivos do MediaFire em seus aplicativos Android de forma rápida e eficiente.

## Códigos de Erros
- `ERROR_INVALID_LINK`: Código de erro para link inválido.<br />
- `ERROR_DOWNLOAD_IN_PROGRESS`: Código de erro para download em progresso.<br />
- `ERROR_FAILED_TO_DOWNLOAD`: Código de erro para falha no download.<br />

## Métodos Públicos
#### `MediaFireDownloader`:
Este construtor inicializa uma nova instância do `MediaFireDownloader` com o contexto fornecido e um listener de download.

| Parâmetro  | Tipo               | Descrição                                                         |
|------------|--------------------|-------------------------------------------------------------------|
| `context`  | `Context`          | O contexto da aplicação, usado para acessar serviços do sistema. |
| `listener` | `DownloadListener` | Um listener que será notificado sobre os eventos de download.     |
>
**Retorno** <br />
Este construtor não retorna nada, mas inicializa a instância do `MediaFireDownloader`.

#### `startDownload`:
Este método inicia o processo de download de um arquivo a partir de uma URL especificada.

| Parâmetro | Tipo    | Descrição                                      |
|-----------|---------|------------------------------------------------|
| `url`     | `String`| A URL do arquivo a ser baixado.               |
| `path`    | `File`  | O caminho onde o arquivo será salvo após o download. |
>
**Retorno** <br />
Este método não retorna nada.

> [!Warning]
> Se o link fornecido for inválido, o método emitirá um erro para `onDownloadFailed` com o valor `ERROR_INVALID_LINK`.

#### `isValidMediaFireUrl`:
Este método verifica se uma URL fornecida é um link válido do MediaFire.

| Parâmetro | Tipo    | Descrição                               |
|-----------|---------|-----------------------------------------|
| `url`     | `String`| A URL a ser verificada.                 |
>
**Retorno** <br />
Este método retorna um valor booleano:
- `true` se a URL for um link válido do MediaFire.
- `false` caso contrário.

#### `startDownload`:
Este método estático inicia o processo de download de um arquivo a partir de uma URL especificada.

| Parâmetro  | Tipo               | Descrição                                                         |
|------------|--------------------|-------------------------------------------------------------------|
| `context`  | `Context`          | O contexto da aplicação, usado para acessar serviços do sistema.  |
| `listener` | `DownloadListener` | Um listener que será notificado sobre os eventos de download.     |
| `url`      | `String`           | A URL do arquivo a ser baixado.                                   |
| `path`     | `File`             | O caminho onde o arquivo será salvo após o download.              |
>
**Retorno** <br />
Este método não retorna nada, mas inicia o processo de download.

> [!Warning]
> Se o link fornecido for inválido, o método emitirá um erro para `onDownloadFailed` com o valor `ERROR_INVALID_LINK`.

#### `isValidMediaFireUrl`:
Este método estático verifica se uma URL fornecida é um link válido do MediaFire.

| Parâmetro | Tipo    | Descrição                               |
|-----------|---------|-----------------------------------------|
| `context` | `Context`| O contexto da aplicação, usado para acessar serviços do sistema. |
| `url`     | `String`| A URL a ser verificada.                 |
>
**Retorno** <br />
Este método retorna um valor booleano:
- `true` se a URL for um link válido do MediaFire.
- `false` caso contrário.

## Métodos Públicos de Ouvinte
A interface `DownloadListener` fornece métodos para ouvir eventos relacionados ao download.

#### `onMediaFireDownloadStart`:
Chamado quando o download é iniciado.

| Parâmetro  | Tipo                   | Descrição                                                        |
|------------|------------------------|------------------------------------------------------------------|
| `downloader` | `MediaFireDownloader` | A instância do `MediaFireDownloader`.                            |
| `url`       | `String`              | A URL do arquivo sendo baixado.                                  |
| `path`      | `File`                | O caminho onde o arquivo será salvo.                             |
>
**Retorno** <br />
Este método não retorna nada.

#### `onMediaFireDownloadComplete`:
Chamado quando o download é concluído com sucesso.

| Parâmetro  | Tipo                   | Descrição                                                        |
|------------|------------------------|------------------------------------------------------------------|
| `downloader` | `MediaFireDownloader` | A instância do `MediaFireDownloader`.                            |
| `url`       | `String`              | A URL do arquivo baixado.                                        |
| `path`      | `File`                | O caminho onde o arquivo foi salvo.                              |
>
**Retorno** <br />
Este método não retorna nada.

#### `onMediaFireDownloadFailed`:
Chamado quando o download falha.

| Parâmetro  | Tipo                   | Descrição                                                        |
|------------|------------------------|------------------------------------------------------------------|
| `downloader` | `MediaFireDownloader` | A instância do `MediaFireDownloader`.                            |
| `url`       | `String`              | A URL do arquivo que falhou ao baixar.                           |
| `path`      | `File`                | O caminho onde o arquivo deveria ter sido salvo.                 |
| `error`     | `int`                 | O código de erro associado à falha no download.                  |
>
**Retorno** <br />
Este método não retorna nada.

#### `onMediaFireDownloadProgressUpdate`:
Chamado periodicamente para atualizar o progresso do download.

| Parâmetro         | Tipo                   | Descrição                                                        |
|-------------------|------------------------|------------------------------------------------------------------|
| `downloader`      | `MediaFireDownloader`  | A instância do `MediaFireDownloader`.                            |
| `url`             | `String`               | A URL do arquivo sendo baixado.                                  |
| `path`            | `File`                 | O caminho onde o arquivo está sendo salvo.                       |
| `bytesDownloaded` | `int`                  | O número de bytes baixados até agora.                            |
| `bytesTotal`      | `int`                  | O tamanho total do arquivo em bytes.                             |
>
**Retorno** <br />
Este método não retorna nada.

# Licença

Este projeto está licenciado sob a GNU Affero General Public License v3.0 (AGPL-3.0), com a seguinte cláusula adicional:

Você não pode vender o software, modificado ou não, independentemente. No entanto, você pode redistribuir, modificar e criar obras derivadas do software desde que mantenha a mesma licença e atribuição, e que essas obras derivadas também sejam distribuídas sem fins comerciais.

Para mais detalhes, veja o arquivo [LICENSE](LICENSE).
