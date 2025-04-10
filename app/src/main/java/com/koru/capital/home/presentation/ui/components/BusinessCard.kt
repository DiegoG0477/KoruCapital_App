package com.koru.capital.home.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage // Import Coil for image loading
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.transformations
import com.composables.icons.lucide.*
import com.koru.capital.R // Assuming sample images are in drawable
import com.koru.capital.core.ui.funnelSansFamily
import com.koru.capital.home.presentation.viewmodel.BusinessCardUiModel
import com.koru.capital.core.ui.theme.* // Import theme colors

@Composable
fun BusinessCard(
    business: BusinessCardUiModel,
    onSaveClick: () -> Unit,
    onLikeClick: () -> Unit,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card( // Wrap content in a Card for better elevation and structure
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onCardClick), // Make the whole card clickable
        shape = RoundedCornerShape(12.dp), // Consistent corner rounding
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = KoruWhite)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // --- Image Section ---
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(business.imageUrl ?: R.drawable.sample_business) // Use actual URL or placeholder
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.sample_business), // Placeholder while loading
                error = painterResource(R.drawable.sample_business), // Placeholder on error
                contentDescription = business.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                // No clip needed here as Card clips content
            )

            // --- Content Padding ---
            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {

                // --- Title ---
                Text(
                    text = business.title,
                    fontSize = 16.sp, // Slightly larger title
                    fontWeight = FontWeight.Medium,
                    fontFamily = funnelSansFamily,
                    textAlign = TextAlign.Start,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis // Handle long titles
                )

                // --- Tags Section ---
                Row(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    // SpaceBetween might create too much space, consider Start or SpacedBy
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    business.category?.let { BusinessTag(icon = Lucide.Tag, text = it) } // Use Tag icon
                    business.location?.let { BusinessTag(icon = Lucide.MapPin, text = it) }
                    business.investmentRange?.let { BusinessTag(icon = Lucide.BadgeDollarSign, text = it) }
                    business.partnerCount?.let {
                        // Only show if count is > 0 or handle 0 explicitly if needed
                        if (it > 0) {
                            BusinessTag(icon = Lucide.Users, text = "$it socios")
                        }
                    }
                }

                // --- Description ---
                Text(
                    modifier = Modifier.padding(top = 9.dp),
                    text = business.description,
                    fontFamily = funnelSansFamily,
                    fontSize = 14.sp,
                    maxLines = 3, // Limit description lines in card view
                    overflow = TextOverflow.Ellipsis,
                    color = KoruDarkGray
                )

                // --- Business Model ---
                Text(
                    modifier = Modifier.padding(top = 12.dp),
                    text = "Modelo de negocio:",
                    color = KoruOrange,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium, // Highlight label slightly
                    fontFamily = funnelSansFamily
                )
                Text(
                    modifier = Modifier.padding(top = 4.dp), // Reduced padding
                    text = business.businessModel,
                    fontFamily = funnelSansFamily,
                    fontSize = 14.sp, // Match description size
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                // --- Owner and Actions Footer ---
                Divider(modifier = Modifier.padding(vertical = 10.dp), color = KoruGrayBackground) // Add visual separator

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Owner Info
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f).padding(end = 8.dp) // Allow shrinking, add padding
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(business.ownerImageUrl ?: R.drawable.sample_profile) // Placeholder if null
                                .crossfade(true)
                                .transformations(coil3.transform.CircleCropTransformation()) // Circle crop
                                .build(),
                            placeholder = painterResource(R.drawable.sample_profile),
                            error = painterResource(R.drawable.sample_profile),
                            contentDescription = "Owner Profile",
                            modifier = Modifier.size(32.dp) // Slightly larger profile pic
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        business.ownerName?.let {
                            Text(
                                text = it,
                                fontFamily = funnelSansFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        // Add Message/LinkedIn icons here if needed later
                        // IconButton(...) { Icon(...) }
                    }

                    // Action Icons (Like, Save)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End // Align icons to the end
                    ) {
                        // Like Button (Placeholder - implement toggle visually later)
                        IconButton(onClick = onLikeClick, modifier = Modifier.size(40.dp)) { // Consistent touch target size
                            Icon(
                                if (business.isLiked) Lucide.Heart else Lucide.Heart, // Use filled icon when liked
                                contentDescription = "Like",
                                tint = if (business.isLiked) KoruRed else KoruDarkGray, // Change color when liked
                                modifier = Modifier.size(21.dp)
                            )
                        }
                        // Save Button with Count
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable(onClick = onSaveClick) // Make row clickable
                                .padding(start = 4.dp) // Add slight space from like
                        ) {
                            IconButton(onClick = onSaveClick, modifier = Modifier.size(40.dp)) {
                                Icon(
                                    if (business.isSaved) Lucide.BookmarkCheck else Lucide.Bookmark, // Use filled icon when saved
                                    contentDescription = "Guardar",
                                    tint = if (business.isSaved) KoruOrange else KoruDarkGray, // Change color when saved
                                    modifier = Modifier.size(21.dp)
                                )
                            }
                            business.savedCount?.let { count ->
                                // Only show count if > 0, or adjust as needed
                                if (count > 0) {
                                    Text(
                                        text = count.toString(),
                                        fontFamily = funnelSansFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = if (business.isSaved) KoruOrange else KoruDarkGray,
                                        modifier = Modifier.padding(start = 0.dp, end = 4.dp) // Adjust padding
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
