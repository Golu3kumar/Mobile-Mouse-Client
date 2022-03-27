package com.selfhelpindia.mobilemouse

class OperationData {
    companion object{
        val OPERATION_MOVE = 0
        val OPERATION_CLICK_DOWN = 1
        val OPERATION_CLICK_UP = 2
        val OPERATION_RIGHT_CLICK = 3
        val OPERATION_SINGLE_CLICK = 6
        val OPERATION_DEL_TEXT = 4
        val OPERATION_TYPE_TEXT = 5
    }

    private var operationKind = -1

    // only valid when operationKind is OPERATION_MOVE
    private var moveX = -1
    private var moveY = -1

    //
    private var inputStr: String? = null

    fun setOperationKind(operationKind:Int) {
        this.operationKind = operationKind
    }



    fun getMoveX(): Int {
        return moveX
    }

    fun setMoveX(moveX: Int) {
        this.moveX = moveX
    }

    fun getMoveY(): Int {
        return moveY
    }

    fun setMoveY(moveY: Int) {
        this.moveY = moveY
    }

    fun getInputStr(): String? {
        return inputStr
    }

    fun setInputStr(inputStr: String?) {
        this.inputStr = inputStr
    }


    override fun toString(): String {
        val oper: String = when (operationKind) {
            OPERATION_CLICK_DOWN -> "click down"
            OPERATION_CLICK_UP -> "click up"
            OPERATION_MOVE -> "move"
            OPERATION_RIGHT_CLICK -> "right click"
            else -> "wrong operation"
        }
        return "OperationData [operationKind=$oper, moveX=$moveX, moveY=$moveY, inputStr=$inputStr]"
    }
}