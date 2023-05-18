package me.ztiany.extenstions.fplearning

import kotlin.math.sqrt

///////////////////////////////////////////////////////////////////////////
// 总结文章 [Thinking functionally in Kotlin](https://blog.kotlin-academy.com/thinking-functionally-in-kotlin-1928c9995643) 中关于函数式编程在 Kotlin 中的应用
///////////////////////////////////////////////////////////////////////////

/*
 问题描述：战斗舰艇发射导弹时需要保持的安全距离。

        1. 如果目标点（敌军舰艇）与舰艇本身（或者友舰）的距离小于等于 Min distance，那么发射的导弹会造成自身（或者友舰）的伤害。
        2. 如果目标点的距离与舰艇本身的距离小于等于 Firing Range，则表示攻击点在导弹的攻击范围之类。

  解决问题：判断在某个点是否可以发射导弹，既能打到目标又不伤害自身。
 */

///////////////////////////////////////////////////////////////////////////
// 命令式编程解决方案
///////////////////////////////////////////////////////////////////////////

/** A data class called Point to store the x and y co-ordinates. */
private data class Point(val xPosition: Double, val yPosition: Double)  // 1

/** A typealias for readability. */
private typealias Position = Point  // 2

/** A class that represents a battleship. */
private class Ship(val position: Position, val minDistance: Double, val range: Double) {  // 3

    /** A function that determines if a Position is within firing range. */
    fun inRange(target: Position, friendly: Position): Boolean { // 4
        val dx = position.xPosition - target.xPosition
        val dy = position.yPosition - target.yPosition

        val friendlyDx = friendly.xPosition - target.xPosition
        val friendlyDy = friendly.yPosition - target.yPosition

        // targetDistance is the distance between the ship and the target.
        val targetDistance = sqrt(dx * dx + dy * dy) // 1

        // friendlyDistance is the distance between the friendly ship and the target.
        val friendlyDistance = sqrt(friendlyDx * friendlyDx + friendlyDy * friendlyDy) //2

        println("inRange---------------------------------------------------------start")
        println("dx = $dx, dy = $dy")
        println("friendlyDx = $friendlyDx, friendlyDy = $friendlyDy")
        println("targetDistance = $targetDistance, friendlyDistance = $friendlyDistance")
        println("range = $range, minDistance = $minDistance")
        println("inRange---------------------------------------------------------end")

        return targetDistance < range // target within the firing distance
                && targetDistance > minDistance // self beyond the safe distance
                && friendlyDistance > minDistance // friend beyond the safe distance
    }

}

///////////////////////////////////////////////////////////////////////////
// 函数式编程的解决方案
///////////////////////////////////////////////////////////////////////////

private typealias inRange = (Position) -> Boolean

/** This function takes the radius as an argument and returns a lambda. Given a point the lambda will return true/false if it is within the radius.  */
private fun circle(radius: Double): inRange {
    return { position ->
        val distance = sqrt(position.xPosition * position.xPosition + position.yPosition * position.yPosition)
        radius > distance
    }
}

/**
 * the function [circle] assumes that the ship is always at the origin.
 * To change this we can either modify this function or create another function that does the transformation.
 */
private fun shift(offset: Position, range: inRange): inRange {
    return { position ->
        //重新计算点的位置
        val dx = position.xPosition - offset.xPosition
        val dy = position.yPosition - offset.yPosition
        //We could use the circle function defined before. This is one of the fundamental building blocks of functional programming.
        range(Position(dx, dy))
    }
}

private fun invert(circle: inRange): inRange {
    // not in circle
    return { position -> !circle(position) }
}

private fun intersection(circle1: inRange, circle2: inRange): inRange {
    // in both circle1 and circle 2
    return { position ->
        circle1(position) && circle2(position)
    }
}

private fun invertIntersection(circle1: inRange, circle2: inRange): inRange {
    // not in both circle1 and circle 2
    return { position ->
        invert(circle1)(position) && invert(circle2)(position)
    }
}

private fun union(circle1: inRange, circle2: inRange): inRange {
    // either in circle1 or circle2
    return { position ->
        circle1(position) || circle2(position)
    }
}

private fun difference(circle1: inRange, circle2: inRange): inRange {
    // points in the first but not in the second
    return { position ->
        intersection(circle1, invert(circle2))(position)
    }
}

// Coming back to our original problem statement we can now construct the solution as show below:
private fun inRange1(
    ownPosition: Position,
    targetPosition: Position,
    friendlyPosition: Position,
    minDistance: Double,
    range: Double
): Boolean {

    return shift(ownPosition, invert(circle(minDistance)))(targetPosition)
            && shift(friendlyPosition, invert(circle(minDistance)))(targetPosition)
            && shift(ownPosition, circle(range))(targetPosition)
}

private fun inRange2(
    ownPosition: Position,
    targetPosition: Position,
    friendlyPosition: Position,
    minDistance: Double,
    range: Double
): Boolean {
    // 合适地开火距离，在 range 内，在 minDistance 外。
    val firingRange = difference(circle(range), circle(minDistance))
    // 某点相对于 ownPosition 需要满足 firingRange。
    val shiftedFiringRange = shift(ownPosition, firingRange)
    // 某点相对于 friendlyPosition 需要满足 circle(minDistance)。
    val friendlyRange = shift(friendlyPosition, circle(minDistance))
    // 用 targetPosition 去应用 shiftedFiringRange 和 friendlyRange
    return difference(shiftedFiringRange, friendlyRange)(targetPosition)
}

///////////////////////////////////////////////////////////////////////////
// 测试
///////////////////////////////////////////////////////////////////////////

fun main() {
    //命令式编程
    val self = Ship(Position(0.0, 0.0), 100.0, 800.0)
    println("can fire: " + self.inRange(Position(500.0, 500.0), Position(50.0, 50.0)))
    //函数式编程
    println(
        "can fire 1: " + inRange1(
            Position(0.0, 0.0),
            Position(500.0, 500.0),
            Position(50.0, 50.0),
            100.0,
            800.0
        )
    )
    println(
        "can fire 2: " + inRange2(
            Position(0.0, 0.0),
            Position(500.0, 500.0),
            Position(50.0, 50.0),
            100.0,
            800.0
        )
    )

}