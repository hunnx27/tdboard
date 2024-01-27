const dataTransfer = new DataTransfer()

export default function () {

    return {
        dataTransfer,
        setFileTemplete(){
            const files = Array.from(dataTransfer.files).map((file, index) => {
                return ({ name: file.name, index })
            });
            var template = document.getElementById("file-template").innerHTML;
            var result = Mustache.render(template, {files});
            document.getElementById("fileList").innerHTML = result;
        
            const fileDeleteBtn = document.querySelectorAll('.file-delete-btn');
            if(fileDeleteBtn){
                fileDeleteBtn.forEach((button)=>{
                    button.addEventListener("click", ()=>{
                         
                        dataTransfer.items.remove(button.dataset.value)
                        // console.log('dataTransfer',dataTransfer.files)
                        const files = Array.from(dataTransfer.files).map((file, index) => {
                            return ({ name: file.name, index })
                        });
                        if(files.length > 0){
                            this.setFileTemplete()
                        }else {
                            document.getElementById("fileList").innerHTML = '';
                        }
                        
                    })
                })
                
            }
        },
        initFileSetting(initFiles){
            if(initFiles){
                for(const item of initFiles) {
                    const blob = new Blob([''], { type: 'application/octet-stream' });
                    const file = new File([blob], item.name, { type: 'application/octet-stream' });
                   // 파일 객체에 커스텀 속성 추가
                    file.id = item.id; 
                    dataTransfer.items.add(file);
            
                }
                this.setFileTemplete()
            }

            const fileInput = document.getElementById('fileInput');
            fileInput.addEventListener('change', ()=>{
                // 선택한 파일 목록 엘리먼트 가져오기
                const fileList = document.getElementById('fileList');
                
                // 선택한 파일 리스트 초기화
                fileList.innerHTML = '';
                
                for (const file of fileInput.files) {
                    dataTransfer.items.add(file);
                }
                // console.log('dataTransfer',dataTransfer.files)
                
                this.setFileTemplete()
            })
        },
        
    }
}


