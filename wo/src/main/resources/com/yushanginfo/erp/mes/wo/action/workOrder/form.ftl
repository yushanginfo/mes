[#ftl]
[@b.head/]
[@b.toolbar title="修改工单信息"]bar.addBack();[/@]
  [@b.form action=b.rest.save(workOrder) theme="list" name="workOrderForm"]
    [@b.textfield name="workOrder.code" label="工单编号" value="${workOrder.code!}" required="true" maxlength="30"/]
    [@b.textfield name="workOrder.batchNum" label="生产批号" value="${workOrder.batchNum!}" required="true" maxlength="30"/]
    [@b.select name="workOrder.orderType.id" label="工单类型" value="${(workOrder.orderType.id)!}" required="true"  style="width:200px;" items=orderTypes empty="..." option="id,name"/]
    [@b.select onchange="fetchScheme(this)" id="productSelect" label="产品信息" name="workOrder.product.id" value=workOrder.product! style="width:400px" empty="..."
            required="true" option="id,title" href="/base/products.json?q={term}&hasTechnicScheme=1" empty="..." /]
    [@b.select label="工单工艺" id="schemeSelect" name="workOrder.technicScheme.id"  style="width:400px"
                required="true" option="id,title" empty="..." /]
    [@b.select label="顾客信息" name="workOrder.customer.id" value=workOrder.customer! style="width:400px"
                required="true" option="id,title" href="/base/customers.json?q={term}" empty="..." /]
    [@b.textfield name="workOrder.amount" label="数量" value="${workOrder.amount!}" required="true" maxlength="80"/]
    [@b.select name="workOrder.factory.id" label="所在厂区" value=workOrder.factory! required="true" tyle="width:200px;" items=factories empty="..."/]
    [@b.datepicker name="workOrder.deadline" label="客户交期" value=(workOrder.deadline)!  required="true"  format="yyyy-MM-dd" /]
    [@b.datepicker name="workOrder.plannedEndOn" label="计划交期" value=(workOrder.plannedEndOn)!  required="true"  format="yyyy-MM-dd" /]
    [@b.textfield name="workOrder.remark" label="备注" value="${workOrder.remark!}" maxlength="190"/]
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
             $("#schemeSelect option[value='${(workOrder.technicScheme.id)!}']").attr("selected","selected");
            }else{
              $('#schemeSelect').empty();
            }
          }
        });
      }
    }
    [#if workOrder.product?? && workOrder.product.id??]
    $(document).ready(function() {
      fetchScheme(document.getElementById("productSelect"));
    });
    [/#if]
  </script>
[@b.foot/]
