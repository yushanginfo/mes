[#ftl/]
[@b.head/]
[@b.toolbar title="导入数据格式有误,错误" + (importResult.errs?size)!0 + "个"]
    bar.addPrint();
    bar.addBack();
[/@]
[#if importResultState! !="importFileError"]
  <div role="alert" class="alert alert-light"> 导入结果：成功：${(importer.success)!}，失败：${(importer.fail)!} </div>
[/#if]
[#if (importResult.errs)??]
  [@b.grid items=(importResult.errs)! var="message" sortable="false" ]
      [@b.row]
        [#if importResultState!!="importFileError"]
            [@b.col title="错误序号" width="10%"]${message_index + 1}[/@]
            [@b.col title="数据行号" property="index" width="10%"]${(message.index)!}[/@]
            [@b.col title="错误内容" width="40%"][#if message.message?starts_with("error")]${b.text(message.message)}[#else]${message.message}[/#if][/@]
            [@b.col title="错误值"][#list message.values as value]${value?default("")}[/#list][/@]
        [#else]
          [@b.col title="错误信息" width="40%"][#if message.message?starts_with("error")]${b.text(message.message)}[#else]${message.message}[/#if][/@]
        [/#if]
      [/@]
      <div style="text-align:center">
        [#if importResultState! == "importFileError"]请使用正确的导入模版，您可以<a href="${downUrl!}"><b>[点击下载]</b></a>模版[/#if]
      </div>
  [/@]
[/#if]
<br>
[#if (importResult.msgs)?? && importResult.msgs?size > 0]
  [@b.grid items=(importResult.msgs)! var="message" sortable="false"]
      [@b.row]
        [#if importResultState!!="importFileError"]
            [@b.col title="序号" width="10%"]${message_index + 1}[/@]
            [@b.col title="行号" property="index" width="10%"]${(message.index + 2)!}[/@]
            [@b.col title="提示内容" width="40%"][#if message.message?starts_with("error")]${b.text(message.message)}[#else]${message.message}[/#if][/@]
            [@b.col title="提示值"][#list message.values as value]${value?default("")}[/#list][/@]
        [#else]

        [/#if]
      [/@]

  [/@]
[/#if]
[@b.foot/]
