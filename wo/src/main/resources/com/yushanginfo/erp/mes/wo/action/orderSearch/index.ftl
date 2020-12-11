[#ftl]
[@b.head/]
[#include "info_macros.ftl"/]
[@info_header title="工单信息"/]
<div class="container">
  <div class="row">
     <div class="col-2">
       <div class="card card-info card-primary card-outline">
         <div class="card-header">
          评审分类统计
         </div>
         [#assign assessMap={}/]
         [#list assessStatuses as s]
           [#assign assessMap=assessMap+{s?string:s.name}/]
         [/#list]
         <div class="card-body">
             <table class="table table-hover table-sm">
               <thead>
                  <th width="60%">状态</th>
                  <th width="40%">数量</th>
               </thead>
               <tbody>
               [#list assessStatusStat as stat]
                <tr>
                 <td>[@b.a href="!search?workOrder.assessStatus="+stat[0] target="order_list"]${assessMap[stat[0]?string]}[/@]</td>
                 <td>${stat[1]}</td>
                </tr>
                [/#list]
               </tbody>
             </table>
         </div>
       </div>

       <div class="card card-info card-primary card-outline">
         <div class="card-header">
          状态分类统计
         </div>
         <div class="card-body">
             <table class="table table-hover table-sm">
               <thead>
                  <th width="60%">状态</th>
                  <th width="40%">数量</th>
               </thead>
               <tbody>
               [#list statusStat as stat]
                <tr>
                 <td>[@b.a href="!search?workOrder.status="+stat[0] target="order_list"]${stat[1]}[/@]</td>
                 <td>${stat[2]}</td>
                </tr>
                [/#list]
               </tbody>
             </table>
         </div>
       </div>

       <div class="card card-info card-primary card-outline">
         <div class="card-header">
          品号分类统计
         </div>
         <div class="card-body">
             <table class="table table-hover table-sm">
               <thead>
                  <th width="60%">品号属性</th>
                  <th width="40%">数量</th>
               </thead>
               <tbody>
               [#list materialTypeStat as stat]
                <tr>
                 <td>[@b.a href="!search?workOrder.product.materialType.id="+stat[0] target="order_list"]${stat[1]}[/@]</td>
                 <td>${stat[2]}</td>
                </tr>
                [/#list]
               </tbody>
             </table>
         </div>
       </div>
     </div><!--end col-2-->
     [@b.div class="col-10" id="order_list" href="!search"]
     [/@]
  </div>
</div>
[@b.foot/]
