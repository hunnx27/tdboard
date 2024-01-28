import createPaginationModule from "/assets/js/pagination.js";
import useAxios from '/assets/js/api/useAxios.js'
import useFilters from '/assets/js/useFilters.js'

const paginationModule = createPaginationModule();
$(function(){

    getEducationsApi(1)
 
    paginationModule.setClickPageNumberHandler((pageNumber)=> {
        console.log(`Page number clicked: ${pageNumber}`);
        getEducationsApi(pageNumber)
     })
})
async function getEducationsApi(pageNumber){
    await useAxios.get('/api/v1/educations',
        {
            page: pageNumber
        }
        ,(res)=> {
        if(res.data.paging.totalElements > 0){
           
            let firstPostNumber = res.data.paging.totalElements - (pageNumber - 1) * res.data.paging.pageSize;
            res.data.contents.map((data)=>{
                const startDate = new Date(data.startDate);
                const endDate = new Date(data.endDate);

                // 일수 계산
                const timeDifferenceInDays = Math.floor((endDate - startDate) / (1000 * 60 * 60 * 24));

                // 총 시간 계산
                const timeDifferenceInHours = Math.floor((endDate - startDate) / (1000 * 60 * 60));
                data.timeDifferenceInDays = timeDifferenceInDays
                data.timeDifferenceInHours = timeDifferenceInHours
                data.createdDateText = useFilters().YYYYMMDD(data.updatedAt || data.createdAt)
                data.no = firstPostNumber
                firstPostNumber --;
            })
            handleSetList(pageNumber, res.data)
        }
    },(err)=> {
        console.log('err',err)
    })

}

function handleSetList(pageNumber, data){
    var template = document.getElementById("table-template").innerHTML;
    var result = Mustache.render(template, data);
    document.getElementById("gallery").innerHTML = result;
    
    paginationModule.setPage(pageNumber,data.paging.pageSize,data.paging.totalElements)
}