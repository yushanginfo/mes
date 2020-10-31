[#ftl]
[@b.head/]
[@b.toolbar title="修改工艺路线"]bar.addBack();[/@]
[@b.tabs]
  [@b.form name="technicSchemeForm" action=b.rest.save(technicScheme) theme="list"]
    [#if (technicScheme.id)?exists]
      [@b.field label="产品编号"]<span style="display: inline-block;">${(technicScheme.product.code)!} ${(technicScheme.product.name)!} </span>[/@]
    [#else]
      [@b.field label="产品编号"]
       <input name="productCode"  placeholder="输入产品编号后，点击页面空白处，即可获取该产品信息">
       <span id="productName"></span>
      [/@]
      [@b.select label="产品信息" id="productSelect" name="technicScheme.product.id"  style="width:400px" required="true"/]
    [/#if]
    [@b.textfield name="technicScheme.indexno" label="编号" value="${technicScheme.indexno!}" required="true" maxlength="80"/]
    [@b.textfield name="technicScheme.name" label="名称" value="${technicScheme.name!}" required="true" maxlength="80"/]
    [@b.select multiple="multiple" label="工艺列表" name="technicIds" items=technics values=technicScheme.technics option=r"${item.code}${item.name} ${(item.machine.name)!}" required="true" style="width:400px"/]
    [@b.formfoot]
      [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
    [/@]
  [/@]
[/@]
[#if !(technicScheme.id)?exists]
  <script>
    $(function() {
      function init(form) {
        var formObj = $(form);
        var productNameObj = formObj.find("#productName");

        formObj.find("[name=productCode]").blur(function() {
          var thisObj = $(this);
          thisObj.parent().find(".error").remove();
          thisObj.parent().next().find(".error").remove();
          productNameObj.empty();
          var code = thisObj.val().trim();
          if (code.length == 0) {
            throwError(thisObj.parent(), "请输入一个有效的产品编号");
            productNameObj.html("<br>");
          } else {
            $.ajax({
              "type": "POST",
              "url": "${b.url("!loadProduct")}",
              "async": false,
              "dataType": "json",
              "data": {
                "q": code
              },
              "success": function(data) {
                $('#productSelect').empty();
                if(data.length>0){
                 for(var i=0;i< data.length;i++){
                   $('#productSelect').append($('<option>', {
                        value: data[i].value,
                        text : data[i].text
                   }));
                 }
                }else{
                  throwError(thisObj.parent().next(), "请输入一个有效的产品编号");
                  productNameObj.html("<br>");
                  thisObj.val("");
                }
              }
            });
          }
        });

        formObj.find(":submit").click(function() {
          var errObj = formObj.find("[name=productCode]").parent().find(".error");
          if (errObj.size()) {
            formObj.find("[name=productCode]").parent().append(errObj);
          }
        });
      }

      function throwError(parentObj, msg) {
        var errObj = parentObj.find(".error");
        if (!errObj.size()) {
          errObj = $("<label>");
          errObj.addClass("error");
          parentObj.append(errObj);
        }
        errObj.text(msg);
      }

      $(document).ready(function() {
        init(document.technicSchemeForm);
      });
    });
  </script>
  [/#if]
[@b.foot/]
