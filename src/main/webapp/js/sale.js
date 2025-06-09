let rowIndex = 0;

function addProductRow() {
    const tbody = document.getElementById('productTableBody');
    const row = document.createElement('tr');
    row.id = 'row_' + rowIndex;
    
    row.innerHTML = `
        <td>
            <select name="productId" onchange="updatePrice(this, ${rowIndex})" required>
                <option value="">请选择商品</option>
                ${products.map(p => `<option value="${p.id}" data-price="${p.price}" data-stock="${p.stock}">${p.name}</option>`).join('')}
            </select>
        </td>
        <td>
            <input type="number" name="unitPrice" step="0.01" readonly id="price_${rowIndex}">
        </td>
        <td>
            <input type="number" name="quantity" min="1" onchange="updateSubtotal(${rowIndex})" id="quantity_${rowIndex}" required>
        </td>
        <td>
            <span id="subtotal_${rowIndex}">￥0.00</span>
        </td>
        <td>
            <button type="button" onclick="removeProductRow(${rowIndex})" class="btn btn-sm btn-danger">删除</button>
        </td>
    `;
    
    tbody.appendChild(row);
    rowIndex++;
}

function removeProductRow(index) {
    const row = document.getElementById('row_' + index);
    if (row) {
        row.remove();
        updateTotal();
    }
}

function updatePrice(selectElement, index) {
    const selectedOption = selectElement.options[selectElement.selectedIndex];
    const priceInput = document.getElementById('price_' + index);
    const quantityInput = document.getElementById('quantity_' + index);
    
    if (selectedOption.value) {
        const price = selectedOption.getAttribute('data-price');
        const stock = selectedOption.getAttribute('data-stock');
        
        priceInput.value = price;
        quantityInput.max = stock;
        quantityInput.placeholder = `库存：${stock}`;
        
        updateSubtotal(index);
    } else {
        priceInput.value = '';
        quantityInput.max = '';
        quantityInput.placeholder = '';
        updateSubtotal(index);
    }
}

function updateSubtotal(index) {
    const priceInput = document.getElementById('price_' + index);
    const quantityInput = document.getElementById('quantity_' + index);
    const subtotalSpan = document.getElementById('subtotal_' + index);
    
    const price = parseFloat(priceInput.value) || 0;
    const quantity = parseInt(quantityInput.value) || 0;
    const subtotal = price * quantity;
    
    subtotalSpan.textContent = '￥' + subtotal.toFixed(2);
    updateTotal();
}

function updateTotal() {
    let total = 0;
    const subtotalSpans = document.querySelectorAll('[id^="subtotal_"]');
    
    subtotalSpans.forEach(span => {
        const value = span.textContent.replace('￥', '');
        total += parseFloat(value) || 0;
    });
    
    document.getElementById('totalAmount').textContent = '￥' + total.toFixed(2);
}

// 表单提交验证
document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('saleForm');
    if (form) {
        form.addEventListener('submit', function(e) {
            const productRows = document.querySelectorAll('#productTableBody tr');
            if (productRows.length === 0) {
                alert('请至少添加一个商品！');
                e.preventDefault();
                return false;
            }
            
            // 检查库存
            let hasError = false;
            productRows.forEach(row => {
                const select = row.querySelector('select[name="productId"]');
                const quantityInput = row.querySelector('input[name="quantity"]');
                
                if (select.value && quantityInput.value) {
                    const selectedOption = select.options[select.selectedIndex];
                    const stock = parseInt(selectedOption.getAttribute('data-stock'));
                    const quantity = parseInt(quantityInput.value);
                    
                    if (quantity > stock) {
                        alert(`商品 ${selectedOption.text} 库存不足！当前库存：${stock}`);
                        hasError = true;
                    }
                }
            });
            
            if (hasError) {
                e.preventDefault();
                return false;
            }
        });
    }
    
    // 初始添加一行
    addProductRow();
});