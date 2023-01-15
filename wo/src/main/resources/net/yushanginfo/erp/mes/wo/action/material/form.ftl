[#ftl]
[@b.head/]
[@b.toolbar title="编辑到料日期"]bar.addBack();[/@]
  [@b.form action=b.rest.save(workOrder) theme="list" name="materialAssessForm"]
    [@b.field label="工单单号"]${workOrder.orderType.code} ${workOrder.batchNum}[/@]
    [@b.field label="产品图号"]${workOrder.product.specification!}[/@]
    [@b.field label="计划数量"]${workOrder.amount}[/@]
    [@b.field label="客户交期"]${(workOrder.deadline?string("yyyy-MM-dd"))!"未设置"}[/@]
    [@b.field label="备注"]${(workOrder.remark)?default("无")}[/@]
  [#if bom?size>0]
    [@b.radios id="ready" label="是否有料" name="materialAssess.ready" value=materialAssess.ready! onclick="displayReadyOn(this)" comment="bom清单中的所有组成是否就绪"/]
    [@b.field label="材料清单"]${workOrder.product.bom?size}个材料, 需填写各自的到料时间[/@]
    [#list bom as i]
      [#assign bomItemComment]${i.material.specification!} [#if i.amount??]${i.amount*workOrder.amount} ${i.material.unit.name}[#else]??[/#if][/#assign]
      [@b.date label=i.indexno+" "+i.material.name comment=bomItemComment name="bom"+i.id+".readyOn" value=(materialAssess.getItemAssess(i).readyOn)! onchange="calcReadOn()"/]
    [/#list]
    [#assign dataRange]建议:${b.now?string('yyyy-MM-dd')}~${(workOrder.deadline?string('yyyy-MM-dd'))!"--"}[/#assign]
    [#-- maxDate="${workOrder.deadline?string('yyyy-MM-dd')}"--]
    [@b.date name="updateAll" label="统一设置为" minDate="${b.now?string('yyyy-MM-dd')}" format="yyyy-MM-dd" comment=dataRange onchange="updateAllReadyOn(this)"/]
    [@b.field label="最终到料日期" ] <span id="readyOn">${(materialAssess.readyOn?string("yyyy-MM-dd"))!'--'}</span>[/@]
  [/#if]
    [#if !workOrder.status.name?contains('完工') && workOrder.assessStatus!="通过" && bom?size >0]
    [@b.formfoot]
      <input type="hidden" name="materialAssess.id" value="${materialAssess.id!}"/>
      [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
    [/@]
    [/#if]
  [/@]
  [#if bom?size>0]
  <script>
    jQuery(function() {
        [#if materialAssess.ready]
        displayReadyOn(document.getElementById("ready_0"));
        [#else]
        displayReadyOn(document.getElementById("ready_1"));
        [/#if]
    })
  function displayReadyOn(ele){
        var hidden=ele.checked && jQuery(ele).val()=='1';
        for(var i=6;i<=8+${bom?size};i++){
          if(hidden){
            jQuery(ele).parentsUntil("ol").parent().children("li:nth("+i+")").hide();
            jQuery("#readyOn").val("");
          }else{
            jQuery(ele).parentsUntil("ol").parent().children("li:nth("+i+")").show();
          }
        }
      }
  function updateAllReadyOn(ele){
    var form=document.materialAssessForm;
    [#list bom as i]
    form["bom${i.id}.readyOn"].value=ele.value
    [/#list]
    jQuery("#readyOn").text(ele.value);
  }

  function calcReadOn(){
    var form=document.materialAssessForm;
    var last="",cur="";
    var hasEmpty=false;
    [#list bom as i]
    cur=form["bom${i.id}.readyOn"].value
    if(cur ){
      if(cur> last) last = cur
    }else{
      hasEmpty=true;
    }
    [/#list]
    if(hasEmpty) jQuery("#readyOn").text("--");
    else jQuery("#readyOn").text(last);
  }
  </script>
  [/#if]
[@b.foot/]
