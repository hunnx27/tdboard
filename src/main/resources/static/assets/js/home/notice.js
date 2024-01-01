import createPaginationModule from "/assets/js/pagination.js";
const paginationModule = createPaginationModule();
$(async function(){
    var data = { name: "Jane", 
                 items: [
                        {no:1, id:1, title: 'K-디지털 플랫폼 홈페이지 개설', date: '2013.12.20'}, 
                        {no:2, id:2, title: 'K-디지털 플랫폼 홈페이지 개설22', date: '2013.12.20'},
                        {no:3, id:2, title: 'K-디지털 플랫폼 홈페이지 개설22', date: '2013.12.20'},
                        {no:4, id:2, title: 'K-디지털 플랫폼 홈페이지 개설22', date: '2013.12.20'},
                        {no:5, id:2, title: 'K-디지털 플랫폼 홈페이지 개설22', date: '2013.12.20'},
                        {no:6, id:2, title: 'K-디지털 플랫폼 홈페이지 개설22', date: '2013.12.20'},
                        {no:7, id:2, title: 'K-디지털 플랫폼 홈페이지 개설22', date: '2013.12.20'},
                        {no:8, id:2, title: 'K-디지털 플랫폼 홈페이지 개설22', date: '2013.12.20'},
                        {no:9, id:2, title: 'K-디지털 플랫폼 홈페이지 개설22', date: '2013.12.20'},
                        {no:10, id:2, title: 'K-디지털 플랫폼 홈페이지 개설22', date: '2013.12.20'},
                    ] ,
                totalCount: 11
               };
    var data2 = { name: "Jane", 
                items: [
                        {no:11, id:2, title: 'K-디지털 플랫폼 홈페이지 개설22', date: '2013.12.20'},
                    ] ,
                totalCount: 11
            };
    // api call 한 다음 결과값 아래 세팅
    handleSetList(1, data)
    
     paginationModule.setClickPageNumberHandler((pageNumber)=> {
        console.log(`Page number clicked: ${pageNumber}`);
        if(pageNumber=== 2){
            handleSetList(pageNumber, data2)
        }else {
            handleSetList(1, data)
        }
        
   
     })
    
})

function handleSetList(pageNumber, data){
    var template = document.getElementById("notice-table-template").innerHTML;
    var result = Mustache.render(template, data);
    document.getElementById("notice-body").innerHTML = result;
    
    paginationModule.setPage(pageNumber,data.totalCount)
}