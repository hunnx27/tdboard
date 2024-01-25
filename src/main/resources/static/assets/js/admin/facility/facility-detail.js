import useAxios from '/assets/js/api/useAxios.js'
import useFilters from '/assets/js/useFilters.js'
import createPaginationModule from "/assets/js/pagination.js";

const paginationModule = createPaginationModule();
window.onload = function() {
    
    const id = document.getElementById('facilityId').value
    getDetailId(id)
    getEquipmentsInRacility(1)

    paginationModule.setClickPageNumberHandler((pageNumber)=> {
        console.log(`Page number clicked: ${pageNumber}`);
        getEquipmentsInRacility(1)
     })
}

async function getDetailId(id) {
    await useAxios.get(`/api/v1/facilities/${id}`,
    {}
    ,(res)=> {

        const customData = {
            ...res.data,
            startDateText : useFilters().YYYYMMDD(res.data.startDate,'YYYY월 MM월 DD일'),
            endDateText : useFilters().YYYYMMDD(res.data.endDate,'YYYY월 MM월 DD일'),
            applicationStartDateText : useFilters().YYYYMMDD(res.data.applicationStartDate,'YYYY월 MM월 DD일'),
            applicationEndDateText : useFilters().YYYYMMDD(res.data.applicationEndDate,'YYYY월 MM월 DD일')
            
        }
        
        var template = document.getElementById("body-template").innerHTML;
        var result = Mustache.render(template, customData);
        document.getElementById("body-template").innerHTML = result;

      
    },(err)=> {
        console.log('err',err)
    })
}

async function getEquipmentsInRacility(pageNumber){
    const id = document.getElementById('facilityId').value
    await useAxios.get(`/api/v1/equipments/in-facility/${id}`,
    {
        page: pageNumber
    }
    ,(res)=> {

        if(res.data.paging.totalElements > 0){
            let firstPostNumber = res.data.paging.totalElements - (pageNumber - 1) * res.data.paging.pageSize;
            res.data.contents.map((data)=>{
               
                data.createdDateText = useFilters().YYYYMMDD(data.updatedAt || data.createdAt)
                data.no = firstPostNumber
                firstPostNumber--;
            })
            handleSetList(pageNumber, res.data)
        }else {
            handleSetList(pageNumber, res.data)
            paginationModule.setPage(1,1,0)
        }

      
    },(err)=> {
        console.log('err',err)
    })
}



function handleSetList(pageNumber, data){
    var template = document.getElementById("table-template").innerHTML;
    var result = Mustache.render(template, data);
    document.getElementById("list-body").innerHTML = result;
    
    paginationModule.setPage(pageNumber,data.paging.pageSize,data.paging.totalElements)
}