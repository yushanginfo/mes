[#ftl]
[@b.head/]
[@b.toolbar title="修改订单信息"]bar.addBack();[/@]
  [@b.form action=b.rest.save(salesOrder) theme="list" name="salesOrderForm"]
    [@b.textfield name="salesOrder.code" label="订单编号" value="${salesOrder.code!}" required="true" maxlength="30"/]
    [@b.textfield name="salesOrder.batchNum" label="生产批号" value="${salesOrder.batchNum!}" required="true" maxlength="30"/]
    [@b.select name="salesOrder.orderType.id" label="订单类型" value="${(salesOrder.orderType.id)!}" required="true"  style="width:200px;" items=orderTypes empty="..." option="id,name"/]
    [@b.select onchange="fetchScheme(this)" id="productSelect" label="产品信息" name="salesOrder.product.id" value=salesOrder.product! style="width:400px" empty="..."
            required="true" option="id,title" href="/base/products.json?q={term}&hasTechnicScheme=1" empty="..." /]
    [@b.select label="工单工艺" id="schemeSelect" name="salesOrder.technicScheme.id"  style="width:400px"
                required="true" option="id,title" empty="..." /]
    [@b.select label="顾客信息" name="salesOrder.customer.id" value=salesOrder.customer! style="width:400px"
                required="true" option="id,title" href="/base/customers.json?q={term}" empty="..." /]
    [@b.textfield name="salesOrder.amount" label="数量" value="${salesOrder.amount!}" required="true" maxlength="80"/]
    [@b.select name="salesOrder.factory.id" label="所在厂区" value=salesOrder.factory! required="true" tyle="width:200px;" items=factories empty="..."/]
    [@b.datepicker name="salesOrder.requireOn" label="计划交付日期" value=(salesOrder.requireOn)!  required="true"  format="yyyy-MM-dd" /]
    [@b.textfield name="salesOrder.remark" label="备注" value="${salesOrder.remark!}" maxlength="190"/]
    [@b.formfoot]
      [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
    [/@]
  [/@]
  <script>
    function fetchScheme(select) {
      var thisObj = $(select);
      var productId = thisObj.val().trim();
      if (productId.length == 0) {
        $('#exchangeStudentSelect').empty();
      } else {
        $.ajax({
          "type": "POST",
          "url": "${b.url("/base/technic-schemes.json?scheme.product.id=")}"+productId,
          "async": false,
          "dataType": "json",
          "success": function(data) {
            $('#schemeSelect').empty();
            if(data.length>0){
             for(var i=0;i< data.length;i++){
               $('#schemeSelect').append($('<option>', {
                    value: data[i].id,
                    text : data[i].title
               }));
             }
             $("#schemeSelect option[value='${(salesOrder.technicScheme.id)!}']").attr("selected","selected");
            }else{
              $('#schemeSelect').empty();
            }
          }
        });
      }
    }
    [#if salesOrder.product?? && salesOrder.product.id??]
    $(document).ready(function() {
      fetchScheme(document.getElementById("productSelect"));
    });
    [/#if]
  </script>
[@b.foot/]
