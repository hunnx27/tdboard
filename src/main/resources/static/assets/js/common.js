/* tab function */
function tab(usetab){
    let tab = document.getElementsByClassName(usetab)
    let tabWrap = document.querySelectorAll('.tab');
    let tabList = tab[0].querySelectorAll('li');
    let tabCon = tab[0].parentNode.querySelectorAll('.tab-con')
    
    function tabOff(){
        tabList.forEach(tabremove => {
            tabremove.classList.remove('on')
        })
        tabCon.forEach(tabconremove => {
            tabconremove.classList.remove('on')
        })
    }
    tabList.forEach( (tabLi,index) => {
        tabLi.addEventListener('click' ,function(e){
            let nodes = e.target.closest('.tab');
            let tabthis = e.target;
            
            if(e.target.classList.contains('on')){
                tabthis.classList.add('on');
            }else if(e.target.parentNode.classList.contains('on')){
                tabthis.parentNode.classList.add('on');
            }else if(e.target.classList.contains('on') == false ){
                tabOff()
                tabList[index].classList.add('on');
                tabCon[index].classList.add('on');
            }else if(e.target.parentNode.classList.contains('on') == false){
                tabOff()
                tabList[index].classList.add('on');
                tabCon[index].classList.add('on');
            }
        });
    });
}
/* selectCustom function */
function selectCustom(){
    var labels = document.querySelectorAll('.dropdown');
    for (let index = 0; index < labels.length; index++) {
        let options = labels[index].querySelectorAll('.dropdown-toggle');
        let optionsList = labels[index].querySelectorAll('li');
        labels[index].addEventListener('click', function(){
            if(labels[index].classList.contains('on')) {
                labels[index].classList.remove('on');
            } else {
                labels.forEach(function(lab){
                    lab.classList.remove('on');
                });
                labels[index].classList.add('on');
                labels[index].focus();
            }
        })
        
        labels[index].querySelectorAll('li').forEach(function(lablesLi){
            lablesLi.addEventListener('click',function(e){
                let textVal = lablesLi.textContent;
                lablesLi.parentNode.previousElementSibling.textContent = textVal
            })
        })
    

    }
    document.addEventListener('click',function(e){
        if(e.target.parentNode.classList.contains('dropdown') == false && e.target.classList.contains('dropdown') == false){
            labels.forEach(function(labelThis){
                labelThis.classList.remove('on');
            })
        }
    })
}
// textareaHeight()  -- textarea 텍스트 추가 자동크기변경
function textareaHeight(){
    const DEFAULT_HEIGHT = 32; // textarea 기본 height
    var textareaNode = document.querySelectorAll('textarea');
    textareaNode.forEach(function(textarea,idx){
        textarea.style.height = 0;
        textarea.style.height = 2 + textarea.scrollHeight + 'px'; 
        textarea.addEventListener('keyup',function(e){
            var $target = textareaNode[idx];
            $target.style.height = 0;
            $target.style.height = 2 + $target.scrollHeight + 'px';
        });
    });
}
/* all checked */
function boardAllChecked(boardcheck){
    console.log(boardcheck.closest('.table'))
    var useTable = boardcheck.closest('.table');
    var useTableTr = useTable.querySelector('tbody');
    var checkboxs = useTableTr.querySelectorAll('input[type="checkbox"]')
    if(boardcheck.checked == true){
        checkboxs.forEach(function(checkbox){
            checkbox.checked = true;
        })
    }else{
        checkboxs.forEach(function(checkbox){
            checkbox.checked = false;
        })
    }
}


window.onload = function(){
    selectCustom();
    //textareaHeight();
    // all check handler
    var allcheckbox = document.querySelectorAll('.board-all-check');
    allcheckbox.forEach(function(checkThis){
        checkThis.addEventListener('click',function(){
            boardAllChecked(checkThis);
        })
        
    })
}
  
    
    
    
    /* tab event */
    

    

   
    //modifyBoard('.board-list-FPL');
