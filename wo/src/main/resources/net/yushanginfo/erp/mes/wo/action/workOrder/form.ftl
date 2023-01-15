[#ftl]
[@b.head/]
[@b.toolbar title="修改工单信息"]bar.addBack();[/@]
  [@b.form action=b.rest.save(workOrder) theme="list" name="workOrderForm"]
    [@b.select name="workOrder.orderType.id" label="工单单别" value="${(workOrder.orderType.id)!}" required="true"  style="width:200px;" items=workOrderTypes empty="..." option="id,name"/]
    [@b.textfield name="workOrder.batchNum" label="工单单号" value="${workOrder.batchNum!}" required="true" maxlength="30" /]
    [@b.select onchange="fetchScheme(this)" id="productSelect" label="产品信息" name="workOrder.product.id" value=workOrder.product! style="width:400px" empty="..."
            required="true" option="id,title" href=ems.base+"/mes/base/products.json?q={term}&hasTechnicScheme=1" empty="..." /]
    [@b.textfield name="workOrder.amount" label="数量" value="${workOrder.amount!}" required="true" maxlength="80"/]
    [@b.select name="workOrder.factory.id" label="所在厂区" value=workOrder.factory! required="true" tyle="width:200px;" items=factories empty="..."/]
    [@b.date name="workOrder.plannedEndOn" label="计划交期" value=(workOrder.plannedEndOn)!  required="true"  format="yyyy-MM-dd" /]
    [#if ((workOrder.assessStatus.name)!'--') != "通过"]
    [@b.date name="workOrder.deadline" label="客户交期" value=(workOrder.deadline)!  required="true"  format="yyyy-MM-dd" /]
    [#else]
    [@b.field label="客户交期"]${(workOrder.deadline?string("yyyy-MM-dd"))!"未设置"}[/@]
    [/#if]
    [@b.textfield name="workOrder.remark" label="备注" value="${workOrder.remark!}" maxlength="190"/]
    [@b.field label="开单日期"]${workOrder.createdAt?string("yyyy-MM-dd")}[/@]
    [@b.field label="状态"]${(workOrder.status.name)!'--'}[/@]
    [@b.field label="评审状态"]${(workOrder.assessStatus.name)!'--'}[/@]
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
          "url": "${b.url(ems.base+"/mes/base/technic-schemes.json?scheme.product.id=")}"+productId,
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
