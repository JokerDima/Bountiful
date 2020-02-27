package ejektaflex.bountiful.api.data.entry

import com.google.gson.annotations.Expose
import ejektaflex.bountiful.api.ext.toEntityEntry
import ejektaflex.bountiful.logic.BountyProgress
import ejektaflex.bountiful.logic.IBountyObjective
import net.minecraft.entity.EntityType
import net.minecraft.nbt.CompoundNBT
import kotlin.math.ceil
import kotlin.math.max

class BountyEntryEntity : BountyEntry(), IBountyObjective {

    @Expose
    override var type: String = BountyType.Entity.ids.first()

    var killedAmount = 0

    override val calculatedWorth: Int
        get() = unitWorth * amount

    override fun pick(worth: Int?): BountyEntry {
        return cloned().apply {
            amount = if (worth != null) {
                max(1, ceil(worth.toDouble() / unitWorth).toInt())
            } else {
                randCount
            }
        }
    }

    override fun validate(): Boolean {
        return true
        // TODO not implemented
    }

    override fun deserializeNBT(tag: CompoundNBT) {
        super.deserializeNBT(tag)
        killedAmount = tag.getInt("killedAmount")
    }


    override fun serializeNBT(): CompoundNBT {
        return super.serializeNBT().apply {
            putInt("killedAmount", killedAmount)
        }
    }

    val entityEntry: EntityType<*>?
        get() {
            return content.toEntityEntry
        }

    override val formattedName: String
        get() = ("(${killedAmount}/${amount}) §a" + "entity." + content + ".name" + " Kills§r")

    override fun tooltipObjective(progress: BountyProgress): String {
        return "§f${progress.stringNums} ${progress.color}${formattedName}§r"
    }

    override fun toString(): String {
        return "BountyEntry (Entity) [Entity: $content, Amount: ${amount}, Weight: $weight]"
    }


}